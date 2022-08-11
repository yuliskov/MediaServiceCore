package com.liskovsoft.youtubeapi.common.converters.regexpandjsonpath.typeadapter;

import android.content.Context;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.liskovsoft.sharedutils.helpers.FileHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.helpers.MessageHelpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.converters.regexpandjsonpath.RegExpAndJsonPath;
import com.liskovsoft.youtubeapi.common.helpers.ReflectionHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpAndJsonPathTypeAdapter<T> {
    private static final String TAG = RegExpAndJsonPathTypeAdapter.class.getSimpleName();
    private final ParseContext mParser;
    private final Class<?> mType;

    public RegExpAndJsonPathTypeAdapter(ParseContext parser, Class<?> type) {
        mParser = parser;
        mType = type;
    }

    public RegExpAndJsonPathTypeAdapter(ParseContext parser, Type type) {
        mParser = parser;
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
        boolean unset = false;

        String regExpVal = null;

        List<Field> fields = ReflectionHelper.getAllFields(type);
        Field regExpField = fields.get(0);
        String[] annotations = getRegExpAndJsonPath(regExpField);

        try {
            Constructor<?> constructor = type.getConstructor();
            obj = constructor.newInstance();

            regExpField.setAccessible(true);
            String path = annotations[0];

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

            } catch (PathNotFoundException e) {
                Log.e(TAG, type.getSimpleName() + ": " + e.getMessage());
            }

            if (regExpVal == null && !ReflectionHelper.isNullable(regExpField)) {
                unset = true; // at least one required field is unset
            }
        } catch (Exception e) {
            unset = true; // at least one field is unset
            e.printStackTrace();
        }

        if (unset) {
            ReflectionHelper.dumpDebugInfo(type, regExpContent);
        }

        InputStream is = new ByteArrayInputStream(regExpVal.getBytes(Charset.forName("UTF-8")));

        String jsonContent = null;

        String jsonPath = annotations[1];

        if (jsonPath != null) { // annotation on the same collection class
            DocumentContext parser = mParser.parse(is);
            try {
                jsonContent = parser.read(jsonPath).toString();
            } catch (PathNotFoundException e) {
                Log.e(TAG, e.getMessage());
            }
        } else { // annotation on field
            jsonContent = Helpers.toString(is);
        }

        T result = (T) readJsonPathType(getGenericType(), jsonContent);

        if (result == null) {
            // Dump root object
            ReflectionHelper.dumpDebugInfo(getGenericType(), jsonContent);
        }

        return result;
    }

    private Object readJsonPathType(Class<?> type, String jsonContent) {
        if (type == null || jsonContent == null) {
            return null;
        }

        Object obj = null;

        try {
            Constructor<?> constructor = type.getConstructor();
            obj = constructor.newInstance();

            DocumentContext parser = mParser.parse(jsonContent);

            List<Field> fields = ReflectionHelper.getAllFields(type);

            for (Field field : fields) {
                processField(field, obj, type, parser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    private boolean processField(Field field, Object obj, Class<?> type, DocumentContext parser) {
        boolean done = false;
        field.setAccessible(true);
        String[] jsonPaths = getRegExpAndJsonPath(field);

        if (jsonPaths == null) {
            return false;
        }

        String[] jsonPath = Arrays.copyOfRange(jsonPaths, 1, jsonPaths.length);

        Object jsonVal = null;

        for (String path : jsonPath) {
            try {
                jsonVal = parser.read(path);
                break;
            } catch (PathNotFoundException e) {
                Log.d(TAG, type.getSimpleName() + ": Path not found: " + path);
            }
        }

        if (jsonVal == null) {
            return false;
        }

        try {
            if (jsonVal instanceof JsonArray) {
                List<Object> list = null;
                Class<?> myType = ReflectionHelper.getGenericParamType(field);

                if (myType == null) {
                    throw new IllegalStateException("Please, supply generic field for the list type: " + field);
                }

                for (Object jsonObj : (JsonArray) jsonVal) {
                    Object item;

                    if (jsonObj instanceof JsonPrimitive) {
                        item = parsePrimitive((JsonPrimitive) jsonObj);
                    } else {
                        item = readJsonPathType(myType, jsonObj.toString());
                    }

                    if (item != null) {
                        if (list == null) {
                            list = new ArrayList<>();
                        }

                        list.add(item);
                    }
                }

                field.set(obj, list);
            } else if (jsonVal instanceof JsonPrimitive) {
                Object val = parsePrimitive((JsonPrimitive) jsonVal);

                field.set(obj, val);
            } else if (jsonVal instanceof JsonObject) {
                Object val = readType(field.getType(), jsonVal.toString());
                field.set(obj, val);
            }

            done = field.get(obj) != null; // field is set
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "%s: Incompatible json value found %s. Same path on different types?", field.getType().getSimpleName(), jsonVal);
        } catch (IllegalAccessException e) {
            Log.d(TAG, "%s: Illegal access with value %s.", field.getType().getSimpleName(), jsonVal);
        }

        return done;
    }

    private String[] getRegExpAndJsonPath(Field field) {
        Annotation[] annotations = field.getAnnotations();

        return getRegExpAndJsonPath(annotations);
    }

    private String[] getRegExpAndJsonPath(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof RegExpAndJsonPath) {
                return ((RegExpAndJsonPath) annotation).value();
            }
        }

        return null;
    }

    private Object parsePrimitive(JsonPrimitive jsonVal) {
        Object val;

        if (jsonVal.isNumber()) {
            if (jsonVal.toString().contains(".")) {
                val = jsonVal.getAsFloat(); // Float
            } else {
                val = jsonVal.getAsInt(); // Integer
            }
        } else if (jsonVal.isBoolean()) {
            val = jsonVal.getAsBoolean(); // Boolean
        } else {
            val = jsonVal.getAsString(); // String
        }

        return val;
    }
}
