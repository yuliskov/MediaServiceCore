package com.liskovsoft.youtubeapi.converters.jsonpath;

import java.io.InputStream;
import java.lang.reflect.Type;

public abstract class TypeAdapter<T> {
    public static TypeAdapter<?> from(Type type) {
        return null;
    }

    public abstract T read(InputStream is);
}
