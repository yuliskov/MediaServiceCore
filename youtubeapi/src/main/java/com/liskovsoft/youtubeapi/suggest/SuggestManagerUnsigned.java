package com.liskovsoft.youtubeapi.suggest;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
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
    Call<BrowseResult> getBrowseResult(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
    Call<NextBrowseResult> getNextBrowseResult(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);
}
