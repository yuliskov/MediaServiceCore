package com.liskovsoft.youtubeapi.converters.jsonpath.typeadapter;

import com.jayway.jsonpath.ParseContext;
import com.liskovsoft.youtubeapi.content.models.ContentTabCollection;

public class ContentTabTypeAdapter extends TypeAdapter<ContentTabCollection> {
    public ContentTabTypeAdapter(ParseContext parser) {
        super(parser);
    }

    /**
     * Type erase workaround
     */
    @Override
    protected Class<?> getGenericType() {
        return ContentTabCollection.class;
    }
}
