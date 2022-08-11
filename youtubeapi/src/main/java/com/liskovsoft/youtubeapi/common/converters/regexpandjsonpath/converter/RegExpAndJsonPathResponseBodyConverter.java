package com.liskovsoft.youtubeapi.common.converters.regexpandjsonpath.converter;

import com.liskovsoft.youtubeapi.common.converters.regexpandjsonpath.typeadapter.RegExpAndJsonPathTypeAdapter;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class RegExpAndJsonPathResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final RegExpAndJsonPathTypeAdapter<T> mAdapter;

    RegExpAndJsonPathResponseBodyConverter(RegExpAndJsonPathTypeAdapter<T> adapter) {
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
