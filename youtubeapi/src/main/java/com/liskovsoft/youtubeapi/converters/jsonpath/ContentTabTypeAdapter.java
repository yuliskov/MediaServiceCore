package com.liskovsoft.youtubeapi.converters.jsonpath;

import com.jayway.jsonpath.ParseContext;
import com.liskovsoft.youtubeapi.content.models.ContentTabCollection;

public class ContentTabTypeAdapter extends TypeAdapter<ContentTabCollection> {
    public ContentTabTypeAdapter(ParseContext parser) {
        super(parser);
    }

    @Override
    protected Class<?> getType() {
        return ContentTabCollection.class;
    }
}
