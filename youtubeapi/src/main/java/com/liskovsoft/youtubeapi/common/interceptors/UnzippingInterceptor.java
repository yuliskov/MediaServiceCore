package com.liskovsoft.youtubeapi.common.interceptors;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;
import okio.GzipSource;
import okio.InflaterSource;
import okio.Okio;
import okio.Source;
import org.brotli.dec.BrotliInputStream;

import java.io.IOException;
import java.util.zip.Inflater;

public class UnzippingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return unzip(response);
    }

    // copied from okhttp3.internal.http.HttpEngine (because is private)
    private Response unzip(final Response response) throws IOException {
        if (response.body() == null) {
            return response;
        }

        //check if we have gzip response
        String contentEncoding = response.headers().get("Content-Encoding");

        //this is used to decompress gzipped responses
        if (contentEncoding != null && contentEncoding.equals("gzip")) {
            Long contentLength = response.body().contentLength();
            GzipSource responseBody = new GzipSource(response.body().source());
            Headers strippedHeaders = response.headers().newBuilder().build();
            return response.newBuilder().headers(strippedHeaders).body(new RealResponseBody(response.body().contentType().toString(), contentLength
                    , Okio.buffer(responseBody))).build();
        } else if (contentEncoding != null && contentEncoding.equals("deflate")) {
            Long contentLength = response.body().contentLength();
            InflaterSource responseBody = new InflaterSource(response.body().source(), new Inflater());
            Headers strippedHeaders = response.headers().newBuilder().build();
            return response.newBuilder().headers(strippedHeaders).body(new RealResponseBody(response.body().contentType().toString(), contentLength
                    , Okio.buffer(responseBody))).build();
        } else if (contentEncoding != null && contentEncoding.equals("br")) {
            Long contentLength = response.body().contentLength();
            Source responseBody = Okio.source(new BrotliInputStream(response.body().source().inputStream()));
            Headers strippedHeaders = response.headers().newBuilder().build();
            return response.newBuilder().headers(strippedHeaders).body(new RealResponseBody(response.body().contentType().toString(), contentLength
                    , Okio.buffer(responseBody))).build();
        } else {
            return response;
        }
    }
}
