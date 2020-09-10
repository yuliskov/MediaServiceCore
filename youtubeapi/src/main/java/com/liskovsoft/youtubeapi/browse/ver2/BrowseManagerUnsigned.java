package com.liskovsoft.youtubeapi.browse.ver2;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.SectionTabResult;
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
    Call<GridTabContinuation> continueGridTab(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<SectionTabResult> getSectionTabResult(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=" + AppConstants.API_KEY)
    Call<SectionTabContinuation> continueSectionTab(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);
}
