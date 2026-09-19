package com.liskovsoft.youtubeapi.search.v2;

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Regression test for the anonymous fallback in {@link SearchService2#getSearch(String, int)}.
 *
 * <p>The service first tries the authenticated request and, when that yields no body (bot check,
 * expired token), retries anonymously. A retrofit {@link Call} is single-shot: {@code OkHttpCall.execute()}
 * raises {@code executed} before dispatching and throws on any later attempt. Reusing the same instance
 * for the retry therefore never falls back - it always throws instead.
 *
 * <p>The contract is pinned with a fake Call rather than a live server, so these tests need no network
 * and no extra test dependency.
 */
public class SearchCallRetryTest {
    /** Mirrors retrofit2.OkHttpCall's single-shot semantics. */
    private static class SingleShotCall<T> implements Call<T> {
        private final List<Response<T>> mResponses;
        private final Request mRequest;
        private boolean mExecuted;
        private int mExecuteCount;

        SingleShotCall(List<Response<T>> responses) {
            mResponses = responses;
            mRequest = new Request.Builder().url("https://www.youtube.com/youtubei/v1/search").build();
        }

        @Override
        public Response<T> execute() {
            // Matches OkHttpCall: the flag is raised before dispatch, so even a failed call is spent.
            if (mExecuted) {
                throw new IllegalStateException("Already executed.");
            }
            mExecuted = true;
            mExecuteCount++;
            return mResponses.remove(0);
        }

        @Override
        public Call<T> clone() {
            return new SingleShotCall<>(mResponses);
        }

        @Override
        public boolean isExecuted() {
            return mExecuted;
        }

        @Override
        public Request request() {
            return mRequest;
        }

        @Override
        public void cancel() {
            // no-op
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public void enqueue(Callback<T> callback) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Timeout timeout() {
            return Timeout.NONE;
        }

        int getExecuteCount() {
            return mExecuteCount;
        }
    }

    private static Response<String> errorResponse() {
        return Response.error(403, ResponseBody.create(MediaType.parse("application/json"), "{}"));
    }

    @SafeVarargs
    private static List<Response<String>> responses(Response<String>... items) {
        return new ArrayList<>(Arrays.asList(items));
    }

    /**
     * NOTE: these exercise the retry through the single-argument RetrofitHelper.get(), not the
     * two-argument auth=false overload the production code uses for the second attempt. The
     * auth=false path calls RetrofitOkHttpHelper.addAuthSkip(), whose class initialiser reaches
     * DeviceHelpers -> android.os.Build.MANUFACTURER and NPEs on a plain JVM. The auth flag is
     * orthogonal to the defect: what breaks the fallback is reusing a spent Call.
     */
    @Test
    public void retryOnAClonedCallReachesTheSecondRequest() {
        SingleShotCall<String> call = new SingleShotCall<>(responses(errorResponse(), Response.success("results")));

        // The shape used by SearchService2.getSearch after the fix.
        String first = RetrofitHelper.get(call);
        String result = first != null ? first : RetrofitHelper.get(call.clone());

        assertEquals("results", result);
    }

    @Test(expected = IllegalStateException.class)
    public void retryOnTheSameCallThrowsInsteadOfFallingBack() {
        SingleShotCall<String> call = new SingleShotCall<>(responses(errorResponse(), Response.success("results")));

        // The pre-fix shape. Documents why the fallback could never run.
        String first = RetrofitHelper.get(call);
        assertNull(first);
        RetrofitHelper.get(call);
    }

    @Test
    public void nonSuccessResponseYieldsNullBodySoTheFallbackIsTriggered() {
        SingleShotCall<String> call = new SingleShotCall<>(responses(errorResponse()));

        // RetrofitHelper.get returns response.body(), which retrofit leaves null for an error
        // response - that is what makes the fallback fire in the first place.
        assertNull(RetrofitHelper.get(call));
        assertEquals(1, call.getExecuteCount());
    }

    @Test
    public void aSpentCallIsMarkedExecutedEvenAfterAFailedResponse() {
        SingleShotCall<String> call = new SingleShotCall<>(responses(errorResponse()));

        RetrofitHelper.get(call);

        // retrofit raises the flag before dispatching, so a 403 still spends the Call - which is
        // why the pre-fix code could never retry, not even on the error path it was written for.
        assertTrue(call.isExecuted());
    }
}
