package com.liskovsoft.youtubeapi.feedback.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class FeedbackResponse {
    @JsonPath("$.feedbackResponses[0].isProcessed")
    private boolean mIsFeedbackProcessed;

    public boolean isFeedbackProcessed() {
        return mIsFeedbackProcessed;
    }
}
