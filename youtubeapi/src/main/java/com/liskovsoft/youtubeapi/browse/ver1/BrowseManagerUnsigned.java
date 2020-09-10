package com.liskovsoft.youtubeapi.browse.ver1;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.browse.ver1.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.ver1.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.RowsTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.TabbedBrowseResult;
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
    Call<BrowseResultContinuation> getContinueBrowseResult(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<TabbedBrowseResult> getTabbedBrowseResult(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<RowsTabContinuation> getContinueTabbedBrowseResult(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);
}
