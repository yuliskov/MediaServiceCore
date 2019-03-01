package com.liskovsoft.youtubeapi.converters.jsonpath;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.ParseContext;
import com.liskovsoft.youtubeapi.content.models.ContentTabCollection;

import java.io.InputStream;
import java.util.List;

public class ContentTabTypeAdapter extends TypeAdapter<ContentTabCollection> {
    private final ParseContext mParser;

    public ContentTabTypeAdapter(ParseContext parser) {
        mParser = parser;
    }

    @Override
    public ContentTabCollection read(InputStream is) {
        DocumentContext parser = mParser.parse(is);
        List<String> tabs = parser.read(getListPath());
        for (String tab : tabs) {
            DocumentContext tabParser = mParser.parse(tab);
        }
        return null;
    }

    private String getListPath() {
        // DocumentContext parser = mParser.parse(value.byteStream());
        return null;
    }
}
