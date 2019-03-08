package com.liskovsoft.youtubeapi.support.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionHelper {
    public static Class<?> getGenericParam(Field field) {
        Type[] params = getGenericParams(field);

        if (params != null && params.length == 1) {
            return (Class<?>) params[0];
        }

        return null;
    }

    public static Type[] getGenericParams(Field field) {
        Type genericFieldType = field.getGenericType();

        if (genericFieldType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericFieldType;

            return aType.getActualTypeArguments();
        }

        return null;
    }

    public static boolean isAssignableFrom(Field field, Class<?> targetType) {
        return field.getType().isAssignableFrom(targetType);
    }
}
