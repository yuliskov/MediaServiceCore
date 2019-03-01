package com.liskovsoft.youtubeapi.converters.jsonpath;

import com.google.gson.JsonArray;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.ParseContext;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class TypeAdapter<T> {
    private final ParseContext mParser;

    public TypeAdapter(ParseContext parser) {
        mParser = parser;
    }

    protected abstract Class<?> getType();

    @SuppressWarnings("unchecked")
    public final T read(InputStream is) {
        DocumentContext parser = mParser.parse(is);
        Object jsonString = parser.read(getJsonPath(getType()));

        return (T) readType(getType(), jsonString);
    }

    @SuppressWarnings("unchecked")
    private Object readType(Class<?> type, Object jsonString) {
        Object obj = null;

        try {
            Constructor<?> constructor = type.getConstructor();
            obj = constructor.newInstance();

            if (obj instanceof List) {
                for (Object jsonObj : (JsonArray) jsonString) {
                    ((List<Object>) obj).add(readType(type.getTypeParameters()[0].getClass(), jsonObj.toString()));
                }

                return obj;
            }

            DocumentContext parser = mParser.parse(jsonString);

            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {
                String jsonPath = getJsonPath(field);

                if (jsonPath == null) {
                    continue;
                }

                Object jsonString2 = parser.read(jsonPath);

                if (jsonString2 instanceof List) {
                    ArrayList<Object> list = new ArrayList<>();
                    for (Object jsonObj : (List) jsonString2) {
                        list.add(readType(field.getType().getTypeParameters()[0].getClass(), jsonObj));
                    }
                    field.set(obj, list);
                } else {
                    field.set(obj, jsonString2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
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
