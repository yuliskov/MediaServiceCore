package com.liskovsoft.googlecommon.common.converters.jsonpath.converter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.liskovsoft.googlecommon.common.converters.jsonpath.converter.JsonPathRequestBodyConverter;
import com.liskovsoft.googlecommon.common.converters.jsonpath.converter.JsonPathResponseBodyConverter;
import com.liskovsoft.googlecommon.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public final class JsonPathConverterFactory extends Converter.Factory {
    private final ParseContext mParser;

    public static JsonPathConverterFactory create() {
        Configuration conf = Configuration
                .builder()
                .mappingProvider(new GsonMappingProvider())
                .jsonProvider(new GsonJsonProvider())
                .build();

        ParseContext parser = JsonPath.using(conf);

        return create(parser);
    }

    private static JsonPathConverterFactory create(ParseContext parser) {
        if (parser == null) {
            throw new NullPointerException("Improper initialization of converter factory");
        }

        return new JsonPathConverterFactory(parser);
    }

    private JsonPathConverterFactory(ParseContext parser) {
        mParser = parser;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type,
                                                            @Nullable Annotation[] annotations,
                                                            @Nullable Retrofit retrofit) {
        return new JsonPathResponseBodyConverter<>(new JsonPathTypeAdapter<>(mParser, type));
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(@NonNull Type type,
                                                          @Nullable Annotation[] parameterAnnotations,
                                                          @Nullable Annotation[] methodAnnotations,
                                                          @Nullable Retrofit retrofit) {
        return new JsonPathRequestBodyConverter<>(new JsonPathTypeAdapter<>(mParser, type));
    }
}
