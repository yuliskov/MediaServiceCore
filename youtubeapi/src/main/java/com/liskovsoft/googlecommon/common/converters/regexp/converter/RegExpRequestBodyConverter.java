package com.liskovsoft.googlecommon.common.converters.regexp.converter;

import com.liskovsoft.googlecommon.common.converters.regexp.typeadapter.RegExpTypeAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.internal.EverythingIsNonNull;

public class RegExpRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.get("text/plain; charset=UTF-8");
    private final RegExpTypeAdapter<T> mAdapter;

    public RegExpRequestBodyConverter(RegExpTypeAdapter<T> adapter) {
        mAdapter = adapter;
    }

    @EverythingIsNonNull
    @Override
    public RequestBody convert(T value) {
        return RequestBody.create(MEDIA_TYPE, value.toString());
    }
}
