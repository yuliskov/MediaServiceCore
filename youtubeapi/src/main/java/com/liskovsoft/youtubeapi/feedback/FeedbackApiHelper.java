package com.liskovsoft.youtubeapi.feedback;

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class FeedbackApiHelper {
    private static final String NOT_INTERESTED_QUERY = "\"feedbackTokens\":[\"%s\"]";

    public static String getNotInterestedQuery(String feedbackToken) {
        String channelTemplate = String.format(NOT_INTERESTED_QUERY, feedbackToken);
        return ServiceHelper.createQueryTV(channelTemplate);
    }
}
