package com.liskovsoft.youtubeapi.common.converters.querystring.converter;

import com.liskovsoft.youtubeapi.common.converters.querystring.typeadapter.TypeAdapter;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class QueryStringResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> mAdapter;

    QueryStringResponseBodyConverter(TypeAdapter<T> adapter) {
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
