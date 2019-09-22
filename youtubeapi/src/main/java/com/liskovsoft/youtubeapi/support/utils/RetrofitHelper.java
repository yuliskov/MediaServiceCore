package com.liskovsoft.youtubeapi.support.utils;

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;
import com.liskovsoft.youtubeapi.BuildConfig;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.converter.JsonPathConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitHelper {
    private static final String DEFAULT_BASE_URL = "https://www.youtube.com"; // ignored when specified url is full

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

            //okBuilder.addInterceptor(new OkHttpProfilerInterceptor());

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(logging);

            OkHttpClient client = okBuilder.build();
            retrofitBuilder.client(client);
        }

        return retrofitBuilder;
    }
}
