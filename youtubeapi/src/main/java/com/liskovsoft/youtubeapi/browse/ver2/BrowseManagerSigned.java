package com.liskovsoft.youtubeapi.browse.ver2;

import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionTabResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For signed users!
 */
public interface BrowseManagerSigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<GridTabResult> getGridTabResult(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionContinuation> continueSection(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<GridTabContinuation> continueGridTab(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionTabResult> getSectionTabResult(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionTabContinuation> continueSectionTab(@Body String browseQuery, @Header("Authorization") String auth);
}
