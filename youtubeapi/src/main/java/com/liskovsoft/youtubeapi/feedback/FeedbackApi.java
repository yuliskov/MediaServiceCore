package com.liskovsoft.youtubeapi.feedback;

import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPath;
import com.liskovsoft.youtubeapi.feedback.models.FeedbackResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For signed users!
 */
@WithJsonPath
public interface FeedbackApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/feedback")
    Call<FeedbackResponse> setNotInterested(@Body String feedbackQuery);
}
