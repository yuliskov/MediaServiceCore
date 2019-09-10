package com.liskovsoft.youtubeapi.support.converters.jsonpath.typeadapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.support.utils.ReflectionHelper;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeAdapter<T> {
    private static final String TAG = TypeAdapter.class.getSimpleName();
    private final ParseContext mParser;
    private final Class<?> mType;

    public TypeAdapter(ParseContext parser, Class<?> type) {
        mParser = parser;
        mType = type;
    }

    public TypeAdapter(ParseContext parser, Type type) {
        mParser = parser;
        mType = (Class<?>) type;
    }

    private Class<?> getGenericType() {
        return mType;
    }

    @SuppressWarnings("unchecked")
    public final T read(InputStream is) {
        Object jsonContent = null;

        String[] jsonPath = getJsonPath(getGenericType());

        if (jsonPath != null) { // annotation on the same collection class
            DocumentContext parser = mParser.parse(is);
            for (String path : jsonPath) {
                try {
                    jsonContent = parser.read(path);
                    break;
                } catch (PathNotFoundException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else { // annotation on field
            jsonContent = Helpers.toString(is);
        }

        return (T) readType(getGenericType(), jsonContent);
    }

    private Object readType(Class<?> type, Object jsonContent) {
        if (type == null || jsonContent == null) {
            return null;
        }

        Object obj = null;
        boolean done = false;

        try {
            Constructor<?> constructor = type.getConstructor();
            obj = constructor.newInstance();

            DocumentContext parser = mParser.parse((String) jsonContent);

            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String[] jsonPath = getJsonPath(field);

                if (jsonPath == null) {
                    continue;
                }

                Object jsonVal = null;

                for (String path : jsonPath) {
                    try {
                        jsonVal = parser.read(path);
                        break;
                    } catch (PathNotFoundException e) {
                        Log.e(TAG, type.getSimpleName() + ": " + e.getMessage());
                    }
                }

                if (jsonVal == null) {
                    continue;
                }

                if (jsonVal instanceof JsonArray) {
                    List<Object> list = new ArrayList<>();
                    Class<?> myType = ReflectionHelper.getGenericParam(field);

                    if (myType == null) {
                        throw new IllegalStateException("Please, supply generic field for the list type: " + field);
                    }

                    for (Object jsonObj : (JsonArray) jsonVal) {
                        Object item = readType(myType, jsonObj.toString());

                        if (item != null) {
                            list.add(item);
                        }
                    }

                    field.set(obj, list);
                } else if (jsonVal instanceof JsonPrimitive) {
                    Object val = null;

                    if (((JsonPrimitive) jsonVal).isNumber()) {
                        val = ((JsonPrimitive) jsonVal).getAsInt();
                    } else if (((JsonPrimitive) jsonVal).isString()) {
                        val = ((JsonPrimitive) jsonVal).getAsString();
                    }

                    field.set(obj, val);
                }

                done = true; // at least one field is set
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return done ? obj : null;
    }

    private String[] getJsonPath(Class<?> type) {
        Annotation[] annotations = type.getAnnotations();

        return getJsonPath(annotations);
    }

    private String[] getJsonPath(Field field) {
        Annotation[] annotations = field.getAnnotations();

        return getJsonPath(annotations);
    }

    private String[] getJsonPath(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof JsonPath) {
                return ((JsonPath) annotation).value();
            }
        }

        return null;
    }
}
