package com.liskovsoft.youtubeapi.next.v1.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class EndScreenRenderer {
    @SerializedName("elements")
    private List<ElementWrapper> mElements;

    public List<ElementWrapper> getElements() {
        return mElements;
    }

    public static class ElementWrapper {
        @SerializedName("endscreenElementRenderer")
        private EndScreenElement mElement;

        public EndScreenElement getElement() {
            return mElement;
        }
    }
}