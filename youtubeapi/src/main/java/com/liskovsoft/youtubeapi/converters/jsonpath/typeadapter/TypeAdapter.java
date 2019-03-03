package com.liskovsoft.youtubeapi.converters.jsonpath.typeadapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPathCollection;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public abstract class TypeAdapter<T> {
    private static final String TAG = TypeAdapter.class.getSimpleName();
    private final ParseContext mParser;

    public TypeAdapter(ParseContext parser) {
        mParser = parser;
    }

    protected abstract Class<?> getGenericType();

    @SuppressWarnings("unchecked")
    public final T read(InputStream is) {
        DocumentContext parser = mParser.parse(is);
        Object jsonObj = parser.read(getJsonPath(getGenericType()));

        return (T) readType(getGenericType(), jsonObj);
    }

    @SuppressWarnings("unchecked")
    private Object readType(Class<?> type, Object jsonContent) {
        Object obj = null;
        boolean done = false;

        try {
            Constructor<?> constructor = type.getConstructor();
            obj = constructor.newInstance();

            if (obj instanceof JsonPathCollection) {
                Class<?> myType = ((JsonPathCollection<Object>) obj).getGenericType();
                for (Object jsonObj : (JsonArray) jsonContent) {
                    ((JsonPathCollection<Object>) obj).add(readType(myType, jsonObj.toString()));
                }

                return obj;
            }

            DocumentContext parser = mParser.parse((String) jsonContent);

            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String jsonPath = getJsonPath(field);

                if (jsonPath == null) {
                    continue;
                }

                Object jsonVal;

                try {
                    jsonVal = parser.read(jsonPath);
                } catch (PathNotFoundException e) {
                    Log.e(TAG, type.getSimpleName() + ": " + e.getMessage());
                    continue;
                }

                if (jsonVal instanceof JsonArray) {
                    JsonPathCollection<Object> list = (JsonPathCollection<Object>) field.get(obj);
                    Class<Object> myType = list.getGenericType();

                    for (Object jsonObj : (JsonArray) jsonVal) {
                        Object item = readType(myType, jsonObj.toString());

                        if (item != null) {
                            list.add(item);
                        }
                    }
                } else if (jsonVal instanceof JsonPrimitive) {
                    Object val = null;

                    if (((JsonPrimitive) jsonVal).isNumber()) {
                        val = ((JsonPrimitive) jsonVal).getAsInt();
                    } else if (((JsonPrimitive) jsonVal).isString()) {
                        val = ((JsonPrimitive) jsonVal).getAsString();
                    }

                    field.set(obj, val);
                    done = true; // at least one field is set
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return done ? obj : null;
    }

    private String getJsonPath(Class<?> type) {
        Annotation[] annotations = type.getAnnotations();

        return getJsonPath(annotations);
    }

    private String getJsonPath(Field field) {
        Annotation[] annotations = field.getAnnotations();

        return getJsonPath(annotations);
    }

    private String getJsonPath(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof JsonPath) {
                return ((JsonPath) annotation).value();
            }
        }

        return null;
    }
}
