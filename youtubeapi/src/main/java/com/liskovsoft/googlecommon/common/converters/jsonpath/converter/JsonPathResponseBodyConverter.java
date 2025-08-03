package com.liskovsoft.googlecommon.common.converters.jsonpath.converter;

import com.liskovsoft.googlecommon.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;

import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public final class JsonPathResponseBodyConverter<T> implements Converter<ResponseBody, T> {
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

    public T convert(InputStream stream) {
        return mAdapter.read(stream);
    }
}
