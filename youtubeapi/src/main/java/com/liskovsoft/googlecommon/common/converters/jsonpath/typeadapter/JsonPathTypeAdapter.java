package com.liskovsoft.googlecommon.common.converters.jsonpath.typeadapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPathObj;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.helpers.ReflectionHelper;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonPathTypeAdapter<T> {
    private static final String TAG = JsonPathTypeAdapter.class.getSimpleName();
    private final ParseContext mParser;
    private final Class<?> mType;

    public JsonPathTypeAdapter(ParseContext parser, Class<?> type) {
        mParser = parser;
        mType = type;
    }

    public JsonPathTypeAdapter(ParseContext parser, Type type) {
        mParser = parser;
        mType = (Class<?>) type;
    }

    @SuppressWarnings("unchecked")
    public final T read(InputStream is) {
        is = process(is);

        String jsonContent = null;

        String[] jsonPath = getJsonPath(getGenericType());

        if (jsonPath != null) { // annotation on the same collection class
            DocumentContext parser = mParser.parse(is);
            for (String path : jsonPath) {
                try {
                    jsonContent = parser.read(path).toString();
                    break;
                } catch (PathNotFoundException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else { // annotation on field
            jsonContent = Helpers.toString(is);
        }

        //long startTimeMs = System.currentTimeMillis();

        T result = (T) readType(getGenericType(), jsonContent);

        if (result == null) {
            // Dump root object
            ReflectionHelper.dumpDebugInfo(getGenericType(), jsonContent);
        }

        //long endTimeMs = System.currentTimeMillis();
        //
        //Log.d(TAG, "Parse time is %s ms", endTimeMs - startTimeMs);

        // Dumping all data for debug purposes
        //ReflectionHelper.dumpDebugInfo(getGenericType(), jsonContent);

        return result;
    }

    /**
     * Enable additional processing like skipping first line etc
     */
    protected InputStream process(InputStream is) {
        return is;
    }

    private Class<?> getGenericType() {
        return mType;
    }

    //private Object readType(Class<?> type, String jsonContent) {
    //    if (type == null || jsonContent == null) {
    //        return null;
    //    }
    //
    //    try {
    //        Constructor<?> constructor = type.getConstructor();
    //        final Object obj = constructor.newInstance();
    //
    //        DocumentContext parser = mParser.parse(jsonContent);
    //
    //        List<Field> fields = ReflectionHelper.getAllFields(type);
    //
    //        List<Boolean> results = RxUtils.fromIterable(fields)
    //                .map(field -> processField(field, obj, type, parser))
    //                .subscribeOn(Schedulers.io())
    //                .observeOn(Schedulers.io())
    //                .toList()
    //                .blockingGet();
    //
    //        for (Boolean result : results) {
    //            // At least one field is set
    //            if (result) {
    //                return obj;
    //            }
    //        }
    //
    //    } catch (Exception e) {
    //        Log.e(TAG, e.getMessage());
    //        e.printStackTrace();
    //    }
    //
    //    return null;
    //}

    private Object readType(Class<?> type, String jsonContent) {
        if (type == null || jsonContent == null) {
            return null;
        }

        Object obj = null;
        boolean done = false;

        try {
            Constructor<?> constructor = type.getConstructor();
            obj = constructor.newInstance();

            DocumentContext parser = mParser.parse(jsonContent);

            List<Field> fields = ReflectionHelper.getAllFields(type);

            for (Field field : fields) {
                boolean result = processField(field, obj, type, parser);

                // At least one field is set
                if (result) {
                    done = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return done ? obj : null;
    }

    private boolean processField(Field field, Object obj, Class<?> type, DocumentContext parser) {
        //Log.d(TAG, "Processing on thread: %s", Thread.currentThread().getName());

        boolean done = false;
        field.setAccessible(true);
        String[] jsonPath = getJsonPath(field);

        if (jsonPath == null) {
            return false;
        }

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
            if (JsonPathObj.class.isAssignableFrom(field.getType())) {
                Object val = readType(field.getType(), jsonVal.toString());
                field.set(obj, val);
            } else if (jsonVal instanceof JsonArray) {
                List<Object> list = null;
                Class<?> myType = ReflectionHelper.getGenericParamType(field);

                if (myType == null) {
                    Log.e(TAG, "Please, supply generic field for the list type: " + field);
                    return false;
                }

                for (Object jsonObj : (JsonArray) jsonVal) {
                    Object item;

                    if (jsonObj instanceof JsonPrimitive) {
                        item = parsePrimitive((JsonPrimitive) jsonObj);
                    } else {
                        item = readType(myType, jsonObj.toString());
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
