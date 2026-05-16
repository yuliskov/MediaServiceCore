package com.liskovsoft.youtubeapi.next.v1.models;

import com.google.gson.annotations.SerializedName;

public class EndScreen {
    @SerializedName("endscreenRenderer")
    private EndScreenRenderer mRenderer;

    public EndScreenRenderer getRenderer() {
        return mRenderer;
    }
}