package com.liskovsoft.youtubeapi.common.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.liskovsoft.youtubeapi.common.converters.gson.GsonClass;
import com.liskovsoft.youtubeapi.common.converters.gson.GsonConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPathClass;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPathSkipClass;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.converter.JsonPathConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.converter.JsonPathSkipConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathSkipTypeAdapter;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import com.liskovsoft.youtubeapi.common.converters.querystring.QueryStringClass;
import com.liskovsoft.youtubeapi.common.converters.querystring.converter.QueryStringConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.regexp.RegExpClass;
import com.liskovsoft.youtubeapi.common.converters.regexp.converter.RegExpConverterFactory;
import com.liskovsoft.youtubeapi.common.models.gen.ErrorResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RetrofitHelper {
    // Ignored when specified url is absolute
    private static final String DEFAULT_BASE_URL = "https://www.youtube.com";

    private static <T> T withGson(Class<T> clazz) {
        return buildRetrofit(GsonConverterFactory.create()).create(clazz);
    }

    private static <T> T withJsonPath(Class<T> clazz) {
        return buildRetrofit(JsonPathConverterFactory.create()).create(clazz);
    }

    /**
     * Skips first line of the response
     */
    private static <T> T withJsonPathSkip(Class<T> clazz) {
        return buildRetrofit(JsonPathSkipConverterFactory.create()).create(clazz);
    }

    private static <T> T withQueryString(Class<T> clazz) {
        return buildRetrofit(QueryStringConverterFactory.create()).create(clazz);
    }

    private static <T> T withRegExp(Class<T> clazz) {
        return buildRetrofit(RegExpConverterFactory.create()).create(clazz);
    }

    public static <T> T get(Call<T> wrapper) {
        Response<T> response = getResponse(wrapper);

        handleErrors(response);

        return response != null ? response.body() : null;
    }

    public static <T> Headers getHeaders(Call<T> wrapper) {
        Response<T> response = getResponse(wrapper);

        return response != null ? response.headers() : null;
    }

    public static <T> Response<T> getResponse(Call<T> wrapper) {
        try {
            return wrapper.execute();
        } catch (ConnectException e) {
            // ConnectException - server is down or address is banned
            // Usually happen on sites like returnyoutubedislikeapi.com
            // We could skip it safe?
            e.printStackTrace();
        } catch (SocketException e) {
            // SocketException - no internet
            // ConnectException - server is down or address is banned
            //wrapper.cancel(); // fix background running when RxJava object is disposed?
            e.printStackTrace();
            throw new IllegalStateException(e); // notify caller about network condition
        } catch (IOException e) {
            // InterruptedIOException - Thread interrupted. Thread died!!
            // UnknownHostException: Unable to resolve host (DNS error) Thread died?
            // Don't rethrow!!! These exceptions cannot be caught inside RxJava!!! Thread died!!!
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

        retrofitBuilder.client(RetrofitOkHttpHelper.getClient());

        return retrofitBuilder;
    }

    /**
     * Get cookie pair: cookieName=cookieValue
     */
    public static <T> String getCookie(Response<T> response, String cookieName) {
        if (response == null) {
            return null;
        }

        List<String> cookies = response.headers().values("Set-Cookie");

        for (String cookie : cookies) {
            if (cookie.startsWith(cookieName)) {
                return cookie.split(";")[0];
            }
        }

        return null;
    }

    public static <T> T create(Class<T> clazz) {
        Annotation[] annotations = clazz.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof RegExpClass) {
                return withRegExp(clazz);
            } else if (annotation instanceof JsonPathClass) {
                return withJsonPath(clazz);
            } else if (annotation instanceof JsonPathSkipClass) {
                return withJsonPathSkip(clazz);
            } else if (annotation instanceof QueryStringClass) {
                return withQueryString(clazz);
            } else if (annotation instanceof GsonClass) {
                return withGson(clazz);
            }
        }

        throw new IllegalStateException("RetrofitHelper: unknown class: " + clazz.getName());
    }

    private static <T> void handleErrors(Response<T> response) {
        if (response == null || response.body() != null) {
            return;
        }

        if (response.code() == 400) {
            Gson gson = new GsonBuilder().create();
            try {
                ErrorResponse error = response.errorBody() != null ? gson.fromJson(response.errorBody().string(), ErrorResponse.class) : null;
                throw new IllegalStateException(error != null && error.getError() != null ? error.getError().getMessage() : "Unknown 400 error");
            } catch (IOException e) {
                // handle failure to read error
            }
        }
    }
}
