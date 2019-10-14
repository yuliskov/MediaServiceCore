package com.liskovsoft.youtubeapi.support.deserializer;

import android.util.Log;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.liskovsoft.youtubeapi.content_old.models.RootContent;

import java.lang.reflect.Type;

public class ContentTabCollectionDeserializer implements JsonDeserializer<RootContent> {
    private static final String TAG = ContentTabCollectionDeserializer.class.getSimpleName();

    @Override
    public RootContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Log.d(TAG, json.toString());
        return null;
    }
}
