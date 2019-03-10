package com.liskovsoft.youtubeapi.support.utils;

import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.converter.JsonPathConverterFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitHelper {
    private static final String DEFAULT_BASE_URL = "https://www.youtube.com"; // ignored when specified url is full

    public static <T> T withGson(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DEFAULT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(clazz);
    }

    public static <T> T withJsonPath(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DEFAULT_BASE_URL)
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
}
