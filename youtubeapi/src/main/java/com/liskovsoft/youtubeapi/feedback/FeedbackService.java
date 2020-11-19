package com.liskovsoft.youtubeapi.feedback;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.feedback.models.FeedbackResponse;
import retrofit2.Call;

public class FeedbackService {
    private static FeedbackService sInstance;
    private final FeedbackManager mFeedbackManager;

    private FeedbackService() {
        mFeedbackManager = RetrofitHelper.withJsonPath(FeedbackManager.class);
    }

    public static FeedbackService instance() {
        if (sInstance == null) {
            sInstance = new FeedbackService();
        }

        return sInstance;
    }

    public void markAsNotInterested(String feedbackToken, String auth) {
        Call<FeedbackResponse> wrapper = mFeedbackManager.setNotInterested(
                FeedbackManagerParams.getNotInterestedQuery(feedbackToken), auth);
        RetrofitHelper.get(wrapper); // ignore result
    }
}
