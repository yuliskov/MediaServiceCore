package com.liskovsoft.googlecommon.common.converters.jsonpath.typeadapter;

import com.jayway.jsonpath.ParseContext;
import com.liskovsoft.googlecommon.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;

import org.apache.commons.io.input.ReaderInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class JsonPathSkipTypeAdapter<T> extends JsonPathTypeAdapter<T> {
    public JsonPathSkipTypeAdapter(ParseContext parser, Class<?> type) {
        super(parser, type);
    }

    public JsonPathSkipTypeAdapter(ParseContext parser, Type type) {
        super(parser, type);
    }

    @Override
    protected InputStream process(InputStream is) {
        return skipFirstLine(is);
    }

    private InputStream skipFirstLine(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            reader.readLine(); // this will read the first line
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ReaderInputStream(reader, "UTF-8");
    }
}
