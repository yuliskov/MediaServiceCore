package com.liskovsoft.googlecommon.common.helpers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class RetrofitAuthHeadersTest {
    @Test
    public void requestsKeepAuthorizationAndPageIdFromOnePublication() throws Exception {
        Map<String, String> original = RetrofitOkHttpHelper.snapshotAuthHeaders();
        Map<String, String> first = headers("first");
        Map<String, String> second = headers("second");
        try {
            RetrofitOkHttpHelper.replaceAuthHeaders(first);
            Map<String, String> oldSnapshot = RetrofitOkHttpHelper.snapshotAuthHeaders();
            RetrofitOkHttpHelper.replaceAuthHeaders(second);
            assertEquals(first, oldSnapshot);

            OkHttpClient client = RetrofitOkHttpHelper.getClient().newBuilder()
                    .dns(host -> { throw new AssertionError("Unexpected network access"); })
                    .addInterceptor(chain -> new Response.Builder()
                            .request(chain.request())
                            .protocol(Protocol.HTTP_1_1)
                            .code(204)
                            .message("Synthetic response")
                            .body(ResponseBody.create(null, ""))
                            .build())
                    .build();
            Request request = new Request.Builder()
                    .url("https://www.youtube.com/youtubei/v1/like")
                    .build();
            AtomicBoolean running = new AtomicBoolean(true);
            AtomicReference<Throwable> writerFailure = new AtomicReference<>();
            CountDownLatch start = new CountDownLatch(1);
            CountDownLatch published = new CountDownLatch(1);
            Thread writer = new Thread(() -> {
                try {
                    start.await();
                    while (running.get()) {
                        RetrofitOkHttpHelper.replaceAuthHeaders(first);
                        published.countDown();
                        RetrofitOkHttpHelper.replaceAuthHeaders(second);
                    }
                } catch (Throwable e) {
                    writerFailure.set(e);
                }
            });
            writer.start();
            try {
                start.countDown();
                assertTrue(published.await(5, TimeUnit.SECONDS));
                for (int i = 0; i < 200; i++) {
                    try (Response response = client.newCall(request).execute()) {
                        assertEquals(204, response.code());
                        String authorization = response.request().header("Authorization");
                        String pageId = response.request().header("X-Goog-Pageid");
                        assertTrue(("Bearer synthetic-first".equals(authorization) && "synthetic-first".equals(pageId))
                                || ("Bearer synthetic-second".equals(authorization) && "synthetic-second".equals(pageId)));
                    }
                }
            } finally {
                running.set(false);
                writer.join();
            }
            assertNull(writerFailure.get());
        } finally {
            RetrofitOkHttpHelper.replaceAuthHeaders(original);
        }
    }

    private static Map<String, String> headers(String account) {
        Map<String, String> result = new HashMap<>();
        result.put("Authorization", "Bearer synthetic-" + account);
        result.put("X-Goog-Pageid", "synthetic-" + account);
        return result;
    }
}
