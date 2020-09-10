package com.liskovsoft.youtubeapi.browse.ver2;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.browse.ver1.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.ver1.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.TabbedBrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuationResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTabContinuationResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTabResult;
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
    Call<GridTabResult> getGridTabResult(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<GridTabContinuationResult> continueGridTabResult(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<RowsTabResult> getRowsTabResult(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<RowsTabContinuationResult> continueRowsTabResult(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);
}
