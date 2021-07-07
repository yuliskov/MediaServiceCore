package com.liskovsoft.youtubeapi.common.helpers;

import android.content.Context;
import com.liskovsoft.sharedutils.helpers.FileHelpers;
import com.liskovsoft.sharedutils.helpers.MessageHelpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionHelper {
    public static Class<?> getGenericParamType(Field field) {
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

    public static List<Field> getAllFields(Class<?> type) {
        Field[] fields = type.getDeclaredFields();

        List<Field> result = new ArrayList<>(Arrays.asList(fields));

        while (type.getSuperclass() != null) { // null if superclass is object
            type = type.getSuperclass();
            result.addAll(Arrays.asList(type.getDeclaredFields()));
        }

        return result;
    }

    public static void setField(Field field, Object obj, Object val) {
        try {
            field.set(obj, val);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void dumpDebugInfo(Class<?> type, String content) {
        dumpDebugInfo(type, FileHelpers.toStream(content));
    }

    public static void dumpDebugInfo(Class<?> type, InputStream content) {
        Context context = GlobalPreferences.sInstance.getContext();

        if (context == null) {
            return;
        }

        File destination = new File(FileHelpers.getCacheDir(context), type.getSimpleName());

        MessageHelpers.showLongMessage(context, String.format("Debug info has been dumped to %s", destination));

        FileHelpers.streamToFile(content, destination);
    }
}
