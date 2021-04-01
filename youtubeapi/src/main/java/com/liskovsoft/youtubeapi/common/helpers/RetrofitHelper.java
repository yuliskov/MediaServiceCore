package com.liskovsoft.youtubeapi.common.helpers;

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.liskovsoft.youtubeapi.BuildConfig;
import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.converter.JsonPathConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.converter.JsonPathSkipConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathSkipTypeAdapter;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import com.liskovsoft.youtubeapi.common.converters.querystring.converter.QueryStringConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.regexp.converter.RegExpConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RetrofitHelper {
    // Ignored when specified url is absolute
    private static final String DEFAULT_BASE_URL = "https://www.youtube.com";
    // Default timeout 10 sec
    private static final long TIMEOUT_SEC = 30;
    public static boolean sForceEnableProfiler;

    public static <T> T withGson(Class<T> clazz) {
        Retrofit.Builder builder = createBuilder();

        Retrofit retrofit = builder
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(clazz);
    }

    public static <T> T withJsonPath(Class<T> clazz) {
        Retrofit.Builder builder = createBuilder();

        Retrofit retrofit = builder
                .addConverterFactory(JsonPathConverterFactory.create())
                .build();

        return retrofit.create(clazz);
    }

    public static <T> T withJsonPathSkip(Class<T> clazz) {
        Retrofit.Builder builder = createBuilder();

        Retrofit retrofit = builder
                .addConverterFactory(JsonPathSkipConverterFactory.create())
                .build();

        return retrofit.create(clazz);
    }

    public static <T> T withQueryString(Class<T> clazz) {
        Retrofit.Builder builder = createBuilder();

        Retrofit retrofit = builder
                .addConverterFactory(QueryStringConverterFactory.create())
                .build();

        return retrofit.create(clazz);
    }

    public static <T> T withRegExp(Class<T> clazz) {
        Retrofit.Builder builder = createBuilder();

        Retrofit retrofit = builder
                .addConverterFactory(RegExpConverterFactory.create())
                .build();

        return retrofit.create(clazz);
    }

    public static <T> T get(Call<T> wrapper) {
        try {
            return wrapper.execute().body();
        } catch (IOException e) {
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

    private static Retrofit.Builder createBuilder() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(DEFAULT_BASE_URL);

        retrofitBuilder.client(createOkHttpClient());

        return retrofitBuilder;
    }
    
    public static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        disableCache(okBuilder);

        setupTimeout(okBuilder);

        addCommonHeaders(okBuilder);

        debugSetup(okBuilder);

        return okBuilder.build();
    }

    private static void disableCache(OkHttpClient.Builder okBuilder) {
        // Disable cache (could help with dlfree error on Eltex)
        okBuilder.cache(null);
    }

    private static void setupTimeout(OkHttpClient.Builder okBuilder) {
        // Default timeout 10 sec
        okBuilder.connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS);
        okBuilder.readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS);
        okBuilder.writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS);
    }

    private static void addCommonHeaders(OkHttpClient.Builder builder) {
        builder.addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.header("User-Agent", AppConstants.APP_USER_AGENT);
            return chain.proceed(requestBuilder.build());
        });
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
}
