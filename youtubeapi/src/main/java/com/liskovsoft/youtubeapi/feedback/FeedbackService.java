package com.liskovsoft.youtubeapi.feedback;

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.feedback.models.FeedbackResponse;
import retrofit2.Call;

public class FeedbackService {
    private static FeedbackService sInstance;
    private final FeedbackApi mFeedbackApi;

    private FeedbackService() {
        mFeedbackApi = RetrofitHelper.create(FeedbackApi.class);
    }

    public static FeedbackService instance() {
        if (sInstance == null) {
            sInstance = new FeedbackService();
        }

        return sInstance;
    }

    public void markAsNotInterested(String feedbackToken) {
        Call<FeedbackResponse> wrapper = mFeedbackApi.setNotInterested(
                FeedbackApiHelper.getNotInterestedQuery(feedbackToken));
        RetrofitHelper.get(wrapper); // ignore result
    }
}
