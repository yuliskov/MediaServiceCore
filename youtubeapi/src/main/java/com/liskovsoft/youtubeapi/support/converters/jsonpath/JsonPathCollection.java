package com.liskovsoft.youtubeapi.support.converters.jsonpath;

import java.util.ArrayList;

public class JsonPathCollection<T> extends ArrayList<T> {
    private final Class<T> mType;

    public JsonPathCollection(Class<T> type) {
        mType = type;
    }

    /**
     * Type erase workaround
     */
    public Class<T> getGenericType() {
        return mType;
    }
}
