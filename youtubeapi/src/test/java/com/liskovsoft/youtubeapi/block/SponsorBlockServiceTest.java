package com.liskovsoft.youtubeapi.block;

import com.liskovsoft.googlecommon.common.converters.jsonpath.converter.JsonPathConverterFactory;
import com.liskovsoft.mediaserviceinterfaces.data.SponsorSegment;
import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.youtubeapi.service.data.YouTubeSponsorSegment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class SponsorBlockServiceTest {
    @Test
    public void notFoundReturnsAnEmptyList() {
        SponsorBlockService service = service(404, "Not Found", null, new AtomicReference<>());

        assertTrue(YouTubeSponsorSegment.from(service.getSegmentList("synthetic-video")).isEmpty());
        assertTrue(YouTubeSponsorSegment.from(service.getSegmentList("synthetic-video",
                Collections.singleton(SponsorSegment.CATEGORY_HOOK))).isEmpty());
    }

    @Test
    public void httpFailuresKeepTheirStatus() {
        for (int status : new int[] {400, 429, 500}) {
            SponsorBlockService service = service(status, "Synthetic failure", null, new AtomicReference<>());

            try {
                service.getSegmentList("synthetic-video");
                fail("Expected an HTTP failure for " + status);
            } catch (HttpException error) {
                assertEquals(status, error.code());
            }
        }
    }

    @Test
    public void ioFailuresKeepTheirCause() {
        for (IOException failure : new IOException[] {
                new ConnectException("Synthetic connection failure"),
                new SocketTimeoutException("Synthetic timeout")}) {
            SponsorBlockService service = service(200, "", failure, new AtomicReference<>());

            try {
                service.getSegmentList("synthetic-video");
                fail("Expected an I/O failure");
            } catch (IllegalStateException error) {
                assertSame(failure, error.getCause());
            }
        }
    }

    @Test
    public void successfulResponseWithoutDataIsNotTreatedAsNoSegments() {
        SponsorBlockService service = service(204, "", null, new AtomicReference<>());

        try {
            service.getSegmentList("synthetic-video");
            fail("Expected missing response data to fail");
        } catch (IllegalStateException error) {
            assertEquals("SponsorBlock response has no segment data", error.getMessage());
        }
    }

    @Test
    public void selectedCategoriesReachTheRequestAndResponseIsParsed() {
        AtomicReference<Request> request = new AtomicReference<>();
        SponsorBlockService service = service(200,
                "[{\"category\":\"hook\",\"actionType\":\"skip\",\"segment\":[1.25,2.5]}]",
                null, request);

        SegmentList result = service.getSegmentList("synthetic-video", Collections.singleton(SponsorSegment.CATEGORY_HOOK));

        assertEquals("synthetic-video", request.get().url().queryParameter("videoID"));
        assertEquals("[\"hook\"]", request.get().url().queryParameter("categories"));
        assertEquals(1, result.getSegments().size());
        assertEquals(SponsorSegment.CATEGORY_HOOK, result.getSegments().get(0).getCategory());
        assertEquals(1250, YouTubeSponsorSegment.from(result).get(0).getStartMs());
    }

    private SponsorBlockService service(int status, String body, IOException failure, AtomicReference<Request> request) {
        OkHttpClient client = new OkHttpClient.Builder()
                .dns(host -> { throw new AssertionError("Unexpected network access"); })
                .addInterceptor(chain -> {
                    request.set(chain.request());
                    if (failure != null) {
                        throw failure;
                    }
                    return new okhttp3.Response.Builder()
                            .request(chain.request())
                            .protocol(Protocol.HTTP_1_1)
                            .code(status)
                            .message("Synthetic response")
                            .body(ResponseBody.create(MediaType.parse("application/json"), body))
                            .build();
                })
                .build();
        SponsorBlockApi api = new Retrofit.Builder()
                .baseUrl("https://example.invalid/")
                .client(client)
                .addConverterFactory(JsonPathConverterFactory.create())
                .build()
                .create(SponsorBlockApi.class);
        return new SponsorBlockService(api);
    }
}
