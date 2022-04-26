package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.guide.Guide;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTabList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabList;
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
    Call<GridTabList> getGridTabList(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<GridTabContinuation> continueGridTab(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionTabList> getSectionTabList(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionTabList> getSectionTabList(@Body String browseQuery, @Header("Authorization") String auth, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionTabContinuation> continueSectionTab(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionTabContinuation> continueSectionTab(@Body String browseQuery, @Header("Authorization") String auth, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionContinuation> continueSection(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionContinuation> continueSection(@Body String browseQuery, @Header("Authorization") String auth, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionList> getSectionList(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionList> getSectionList(@Body String browseQuery, @Header("Authorization") String auth, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/guide")
    Call<Guide> getGuide(@Body String browseQuery, @Header("Authorization") String auth);
}
