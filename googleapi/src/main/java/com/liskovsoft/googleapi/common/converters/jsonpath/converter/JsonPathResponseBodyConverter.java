package com.liskovsoft.googleapi.common.converters.jsonpath.converter;

import com.liskovsoft.googleapi.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class JsonPathResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final JsonPathTypeAdapter<T> mAdapter;

    JsonPathResponseBodyConverter(JsonPathTypeAdapter<T> adapter) {
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
