package com.liskovsoft.youtubeapi.browse.v1;

import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.guide.Guide;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTabList;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionTabList;
import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPath;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For signed users!
 */
@WithJsonPath
public interface BrowseApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<GridTabList> getGridTabList(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<GridTabContinuation> continueGridTab(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionTabList> getSectionTabList(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionTabList> getSectionTabList(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionTabContinuation> continueSectionTab(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionTabContinuation> continueSectionTab(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionContinuation> continueSection(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionContinuation> continueSection(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionList> getSectionList(@Body String browseQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<SectionList> getSectionList(@Body String browseQuery, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/guide")
    Call<Guide> getGuide(@Body String browseQuery);
}
