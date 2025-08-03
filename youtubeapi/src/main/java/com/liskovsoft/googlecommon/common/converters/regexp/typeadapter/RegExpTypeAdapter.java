package com.liskovsoft.googlecommon.common.converters.regexp.typeadapter;

import android.content.Context;
import com.jayway.jsonpath.PathNotFoundException;
import com.liskovsoft.sharedutils.helpers.FileHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.helpers.MessageHelpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;
import com.liskovsoft.googlecommon.common.helpers.ReflectionHelper;

import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpTypeAdapter<T> {
    private static final String TAG = RegExpTypeAdapter.class.getSimpleName();
    private final Class<?> mType;

    public RegExpTypeAdapter(Class<?> type) {
        mType = type;
    }

    public RegExpTypeAdapter(Type type) {
        mType = (Class<?>) type;
    }

    private Class<?> getGenericType() {
        return mType;
    }

    @SuppressWarnings("unchecked")
    public final T read(InputStream is) {
        // Can't use Scanner(is) here. Because pattern should be matched multiple times.
        String regExpContent = Helpers.toString(is);

        return (T) readType(getGenericType(), regExpContent);
    }

    private Object readType(Class<?> type, String regExpContent) {
        if (type == null || regExpContent == null) {
            return null;
        }

        Object obj = null;
        boolean done = false;
        boolean unset = false;

        try {
            Constructor<?> constructor = type.getConstructor();
            obj = constructor.newInstance();

            List<Field> fields = ReflectionHelper.getAllFields(type);

            for (Field field : fields) {
                field.setAccessible(true);
                String[] regExp = getRegExp(field);

                if (regExp == null) {
                    continue;
                }

                String regExpVal = null;

                for (String path : regExp) {
                    try {
                        Pattern pattern = Pattern.compile(path);
                        Matcher matcher = pattern.matcher(regExpContent);

                        if (matcher.find()) {
                            if (matcher.groupCount() >= 1) {
                                regExpVal = matcher.group(1);
                            } else {
                                regExpVal = matcher.group(0); // all match
                            }
                        }

                        // Match found
                        if (regExpVal != null) {
                            break;
                        }
                    } catch (PathNotFoundException e) {
                        Log.e(TAG, type.getSimpleName() + ": " + e.getMessage());
                    }
                }

                if (regExpVal == null && !ReflectionHelper.isNullable(field)) {
                    unset = true; // at least one required field is unset
                    continue;
                }

                field.set(obj, regExpVal);

                done = true; // at least one field is set
            }
        } catch (Exception e) {
            unset = true; // at least one field is unset
            e.printStackTrace();
        }

        if (unset) {
            ReflectionHelper.dumpDebugInfo(type, regExpContent);
        }

        return done ? obj : null;
    }

    private String[] getRegExp(Class<?> type) {
        Annotation[] annotations = type.getAnnotations();

        return getRegExp(annotations);
    }

    private String[] getRegExp(Field field) {
        Annotation[] annotations = field.getAnnotations();

        return getRegExp(annotations);
    }

    private String[] getRegExp(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof RegExp) {
                return ((RegExp) annotation).value();
            }
        }

        return null;
    }
}
