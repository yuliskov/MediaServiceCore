package com.liskovsoft.youtubeapi.support.converters.jsonpath.converter;

import com.liskovsoft.youtubeapi.support.converters.jsonpath.typeadapter.TypeAdapter;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class JsonPathResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> mAdapter;

    JsonPathResponseBodyConverter(TypeAdapter<T> adapter) {
        mAdapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) {
        try {
            return mAdapter.read(value.byteStream());
        } finally {
            value.close();
        }
    }
}
