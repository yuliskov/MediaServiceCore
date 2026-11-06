package com.liskovsoft.youtubeapi.block;

import com.liskovsoft.googlecommon.common.converters.jsonpath.converter.JsonPathConverterFactory;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.youtubeapi.block.data.SegmentList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class SponsorBlockRequestSecurityTest {
    @Test
    public void bothEndpointsExcludeYouTubeAuthentication() throws IOException {
        Map<String, String> authHeaders = RetrofitOkHttpHelper.getAuthHeaders();
        Map<String, String> originalHeaders = new HashMap<>(authHeaders);
        try {
            authHeaders.clear();
            authHeaders.put("Authorization", "Bearer synthetic-test-token");
            authHeaders.put("Cookie", "SID=synthetic-test-cookie");
            authHeaders.put("X-Goog-AuthUser", "0");
            authHeaders.put("X-Goog-Pageid", "synthetic-test-page");
            authHeaders.put("X-Goog-Visitor-Id", "synthetic-test-visitor");

            AtomicReference<Request> request = new AtomicReference<>();
            OkHttpClient client = RetrofitOkHttpHelper.getClient().newBuilder()
                    .dns(host -> { throw new AssertionError("Unexpected network access"); })
                    .addInterceptor(chain -> {
                        request.set(chain.request());
                        return new okhttp3.Response.Builder()
                                .request(chain.request())
                                .protocol(Protocol.HTTP_1_1)
                                .code(204)
                                .message("Synthetic response")
                                .body(ResponseBody.create(null, ""))
                                .build();
                    })
                    .build();
            SponsorBlockApi api = new Retrofit.Builder()
                    .baseUrl("https://example.invalid/")
                    .client(client)
                    .addConverterFactory(JsonPathConverterFactory.create())
                    .build()
                    .create(SponsorBlockApi.class);

            assertUnauthenticated(api.getSegments("synthetic-video"), request, "sponsor.ajay.app");
            assertUnauthenticated(api.getSegments2("synthetic-video"), request, "api.sponsor.ajay.app");
        } finally {
            authHeaders.clear();
            authHeaders.putAll(originalHeaders);
        }
    }

    private void assertUnauthenticated(Call<SegmentList> call, AtomicReference<Request> captured, String host)
            throws IOException {
        assertEquals(204, call.execute().code());
        Request request = captured.get();
        assertEquals(host, request.url().host());
        assertNull(request.header("Authorization"));
        assertNull(request.header("Cookie"));
        assertNull(request.header("X-Goog-AuthUser"));
        assertNull(request.header("X-Goog-Pageid"));
        assertNull(request.header("X-Goog-Visitor-Id"));
        assertNull(request.url().queryParameter("key"));
    }
}
