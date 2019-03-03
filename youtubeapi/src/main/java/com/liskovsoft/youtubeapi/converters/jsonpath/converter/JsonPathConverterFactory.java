package com.liskovsoft.youtubeapi.converters.jsonpath.converter;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.liskovsoft.youtubeapi.converters.jsonpath.typeadapter.ContentTabTypeAdapter;
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

    public static JsonPathConverterFactory create(ParseContext parser) {
        if (parser == null) {
            throw new NullPointerException();
        }

        return new JsonPathConverterFactory(parser);
    }

    private JsonPathConverterFactory(ParseContext parser) {
        mParser = parser;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new JsonPathResponseBodyConverter<>(new ContentTabTypeAdapter(mParser));
    }
    
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        return new JsonPathRequestBodyConverter<>(new ContentTabTypeAdapter(mParser));
    }
}
