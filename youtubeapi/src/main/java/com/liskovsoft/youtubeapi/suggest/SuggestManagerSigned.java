package com.liskovsoft.youtubeapi.suggest;

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
 * For signed users!
 */
public interface SuggestManagerSigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<BrowseResult> getBrowseResult(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<NextBrowseResult> getNextBrowseResult(@Body String browseQuery, @Header("Authorization") String auth);
}
