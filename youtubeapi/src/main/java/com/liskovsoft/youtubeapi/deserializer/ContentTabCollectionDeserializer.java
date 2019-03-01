package com.liskovsoft.youtubeapi.deserializer;

import android.util.Log;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.liskovsoft.youtubeapi.content.models.ContentTabCollection;

import java.lang.reflect.Type;

public class ContentTabCollectionDeserializer implements JsonDeserializer<ContentTabCollection> {
    private static final String TAG = ContentTabCollectionDeserializer.class.getSimpleName();

    @Override
    public ContentTabCollection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Log.d(TAG, json.toString());
        return null;
    }
}
