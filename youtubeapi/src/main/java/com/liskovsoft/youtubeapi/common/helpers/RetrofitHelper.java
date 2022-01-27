package com.liskovsoft.youtubeapi.common.helpers;

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.okhttp.OkHttpCommons;
import com.liskovsoft.youtubeapi.BuildConfig;
import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.common.converters.gson.GsonConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.converter.JsonPathConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.converter.JsonPathSkipConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathSkipTypeAdapter;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import com.liskovsoft.youtubeapi.common.converters.querystring.converter.QueryStringConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.regexp.converter.RegExpConverterFactory;
import com.liskovsoft.youtubeapi.common.interceptors.UnzippingInterceptor;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.dnsoverhttps.DnsOverHttps;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class RetrofitHelper {
    // Ignored when specified url is absolute
    private static final String DEFAULT_BASE_URL = "https://www.youtube.com";
    public static boolean sForceEnableProfiler;
    private static OkHttpClient sOkHttpClient;

    public static <T> T withGson(Class<T> clazz) {
        return buildRetrofit(GsonConverterFactory.create()).create(clazz);
    }

    public static <T> T withJsonPath(Class<T> clazz) {
        return buildRetrofit(JsonPathConverterFactory.create()).create(clazz);
    }

    /**
     * Skips first line of the response
     */
    public static <T> T withJsonPathSkip(Class<T> clazz) {
        return buildRetrofit(JsonPathSkipConverterFactory.create()).create(clazz);
    }

    public static <T> T withQueryString(Class<T> clazz) {
        return buildRetrofit(QueryStringConverterFactory.create()).create(clazz);
    }

    public static <T> T withRegExp(Class<T> clazz) {
        return buildRetrofit(RegExpConverterFactory.create()).create(clazz);
    }

    public static <T> T get(Call<T> wrapper) {
        try {
            return wrapper.execute().body();
        } catch (IOException e) {
            //wrapper.cancel(); // fix background running when RxJava object is disposed
            e.printStackTrace();
        }

        return null;
    }

    public static <T> JsonPathTypeAdapter<T> adaptJsonPathSkip(Class<?> clazz) {
        Configuration conf = Configuration
                .builder()
                .mappingProvider(new GsonMappingProvider())
                .jsonProvider(new GsonJsonProvider())
                .build();

        ParseContext parser = JsonPath.using(conf);

        return new JsonPathSkipTypeAdapter<>(parser, clazz);
    }

    public static Retrofit buildRetrofit(Converter.Factory factory) {
        Retrofit.Builder builder = createBuilder();

        return builder
                .addConverterFactory(factory)
                .build();
    }

    private static Retrofit.Builder createBuilder() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(DEFAULT_BASE_URL);

        retrofitBuilder.client(createOkHttpClient());

        return retrofitBuilder;
    }

    private static OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            sOkHttpClient = createOkHttpClient();
        }

        return sOkHttpClient;
    }

    public static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        //disableCache(okBuilder);

        // Cause hangs and crashes (especially on Android 8 devices or Dune HD)
        //forceIPv4Dns(okBuilder);

        OkHttpCommons.setupConnectionFix(okBuilder);

        OkHttpCommons.setupConnectionParams(okBuilder);

        addCommonHeaders(okBuilder);

        enableDecompression(okBuilder);

        debugSetup(okBuilder);

        return okBuilder.build();
    }

    private static void disableCache(OkHttpClient.Builder okBuilder) {
        // Disable cache (could help with dlfree error on Eltex)
        // Spoiler: no this won't help with dlfree error on Eltex
        okBuilder.cache(null);
    }

    private static void addCommonHeaders(OkHttpClient.Builder builder) {
        builder.addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.header("User-Agent", AppConstants.APP_USER_AGENT);

            // Enable compression in production
            requestBuilder.header("Accept-Encoding", BuildConfig.DEBUG ?
                    AppConstants.ACCEPT_ENCODING_IDENTITY : AppConstants.ACCEPT_ENCODING_DEFAULT);

            // Emulate browser request
            //requestBuilder.header("Connection", "keep-alive");
            //requestBuilder.header("Cache-Control", "max-age=0");
            requestBuilder.header("Referer", "https://www.youtube.com/tv");

            return chain.proceed(requestBuilder.build());
        });
    }

    /**
     * Checks that response is compressed and do uncompress if needed.
     */
    private static void enableDecompression(Builder builder) {
        // Add gzip/deflate/br support
        //builder.addInterceptor(BrotliInterceptor.INSTANCE);
        builder.addInterceptor(new UnzippingInterceptor());
    }

    private static void debugSetup(OkHttpClient.Builder okBuilder) {
        if (BuildConfig.DEBUG) {
            // Force enable for unit tests.
            // If you enable it to all requests - expect slowdowns.
            //if (sForceEnableProfiler) {
            //    addProfiler(okBuilder);
            //}

            addProfiler(okBuilder);

            addLogger(okBuilder);
        }
    }

    private static void addProfiler(OkHttpClient.Builder okBuilder) {
        okBuilder.addInterceptor(new OkHttpProfilerInterceptor());
    }

    private static void addLogger(OkHttpClient.Builder okBuilder) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okBuilder.addInterceptor(logging);
    }

    private static void forceIPv4Dns(OkHttpClient.Builder okBuilder) {
        okBuilder.dns(hostname -> {
            List<InetAddress> lookup = Dns.SYSTEM.lookup(hostname);
            List<InetAddress> filter = Helpers.filter(lookup, value -> value instanceof Inet4Address);
            return filter != null ? filter : lookup;
        });
    }

    /**
     * Usage: <code>OkHttpClient newClient = wrapDns(client)</code><br/>
     * https://github.com/square/okhttp/blob/master/okhttp-dnsoverhttps/src/test/java/okhttp3/dnsoverhttps/DohProviders.java
     */
    public static OkHttpClient wrapDns(OkHttpClient client) {
        return client.newBuilder().dns(buildGoogle(client)).build();
    }

    private static DnsOverHttps buildGoogle(OkHttpClient bootstrapClient) {
        return new DnsOverHttps.Builder().client(bootstrapClient)
                .url(HttpUrl.get("https://dns.google/dns-query"))
                .bootstrapDnsHosts(getByIp("8.8.4.4"), getByIp("8.8.8.8"))
                .build();
    }

    private static InetAddress getByIp(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            // unlikely
            throw new RuntimeException(e);
        }
    }
}
