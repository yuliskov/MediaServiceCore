package com.liskovsoft.googlecommon.common.converters.jsonpath.converter;

import android.util.Log;
import com.liskovsoft.googlecommon.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.internal.EverythingIsNonNull;

public class JsonPathRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final String TAG = JsonPathRequestBodyConverter.class.getSimpleName();
    private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");
    private final JsonPathTypeAdapter<T> mAdapter;

    public JsonPathRequestBodyConverter(JsonPathTypeAdapter<T> adapter) {
        mAdapter = adapter;
    }

    @EverythingIsNonNull
    @Override
    public RequestBody convert(T value) {
        Log.d(TAG, value.toString());
        return RequestBody.create(MEDIA_TYPE, value.toString());
    }
}
