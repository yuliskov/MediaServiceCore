package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.NextTabbedBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For unsigned users!
 */
public interface BrowseManagerUnsigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<BrowseResult> getBrowseResult(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<NextBrowseResult> getNextBrowseResult(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<TabbedBrowseResult> getTabbedBrowseResult(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<NextTabbedBrowseResult> getNextTabbedBrowseResult(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);
}
