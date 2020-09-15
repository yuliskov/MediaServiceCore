package com.liskovsoft.youtubeapi.common.converters.querystring.converter;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryString;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory;
import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class QueryStringResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final JsonPathTypeAdapter<T> mAdapter;

    QueryStringResponseBodyConverter(JsonPathTypeAdapter<T> adapter) {
        mAdapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) {
        try {
            UrlQueryString queryString = UrlQueryStringFactory.parse(value.byteStream());
            return mAdapter.read(Helpers.toStream(queryString.get(AppConstants.VIDEO_INFO_JSON_CONTENT_PARAM)));
        } finally {
            value.close();
        }
    }
}
