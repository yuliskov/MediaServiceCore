package com.liskovsoft.youtubeapi.common.helpers;

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;
import com.liskovsoft.youtubeapi.BuildConfig;
import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.converter.JsonPathConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.converter.JsonPathSkipConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.querystring.converter.QueryStringConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.regexp.converter.RegExpConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitHelper {
    private static final String DEFAULT_BASE_URL = "https://www.youtube.com"; // ignored when specified url is full
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

    private static Retrofit.Builder createBuilder() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(DEFAULT_BASE_URL);

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        addCommonHeaders(okBuilder);

        debugSetup(okBuilder);

        OkHttpClient client = okBuilder.build();

        retrofitBuilder.client(client);

        return retrofitBuilder;
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

            // Disable cache (could help with dlfree error on Eltex)
            //okBuilder.cache(null);
        }
    }

    private static void addCommonHeaders(OkHttpClient.Builder builder) {
        builder.addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.header("User-Agent", AppConstants.APP_USER_AGENT);
            return chain.proceed(requestBuilder.build());
        });
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
