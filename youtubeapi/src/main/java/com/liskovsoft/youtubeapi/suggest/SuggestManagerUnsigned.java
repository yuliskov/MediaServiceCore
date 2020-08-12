package com.liskovsoft.youtubeapi.suggest;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.suggest.models.NextSuggestResult;
import com.liskovsoft.youtubeapi.suggest.models.SuggestResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For unsigned users!
 */
public interface SuggestManagerUnsigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
    Call<SuggestResult> getSuggestResult(@Body String suggestQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
    Call<NextSuggestResult> getNextSuggestResult(@Body String suggestQuery, @Header("X-Goog-Visitor-Id") String visitorId);
}
