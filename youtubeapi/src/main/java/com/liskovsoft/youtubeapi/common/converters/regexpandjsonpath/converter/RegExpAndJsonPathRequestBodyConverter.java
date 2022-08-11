package com.liskovsoft.youtubeapi.common.converters.regexpandjsonpath.converter;

import android.util.Log;
import com.liskovsoft.youtubeapi.common.converters.regexpandjsonpath.typeadapter.RegExpAndJsonPathTypeAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.internal.EverythingIsNonNull;

public class RegExpAndJsonPathRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final String TAG = RegExpAndJsonPathRequestBodyConverter.class.getSimpleName();
    private static final MediaType MEDIA_TYPE = MediaType.get("text/plain; charset=UTF-8");
    private final RegExpAndJsonPathTypeAdapter<T> mAdapter;

    public RegExpAndJsonPathRequestBodyConverter(RegExpAndJsonPathTypeAdapter<T> adapter) {
        mAdapter = adapter;
    }

    @EverythingIsNonNull
    @Override
    public RequestBody convert(T value) {
        Log.d(TAG, value.toString());
        return RequestBody.create(MEDIA_TYPE, value.toString());
    }
}
