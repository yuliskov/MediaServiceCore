package com.liskovsoft.youtubeapi.common.helpers;

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;
import com.liskovsoft.youtubeapi.BuildConfig;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.converter.JsonPathConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.querystring.converter.QueryStringConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.regexp.converter.RegExpConverterFactory;
import okhttp3.OkHttpClient;
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

        if (BuildConfig.DEBUG) {
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

            if (sForceEnableProfiler) { // force enable for unit tests
                okBuilder.addInterceptor(new OkHttpProfilerInterceptor());
            }

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(logging);

            // Disable cache (could help with dlfree error on Eltex)
            //okBuilder.cache(null);

            OkHttpClient client = okBuilder.build();
            retrofitBuilder.client(client);
        }

        return retrofitBuilder;
    }
}
