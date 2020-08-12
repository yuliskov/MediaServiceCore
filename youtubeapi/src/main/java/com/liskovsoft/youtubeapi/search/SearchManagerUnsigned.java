package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Example url: https://www.youtube.com/youtubei/v1/search?key=<API_KEY>
 */
public interface SearchManagerUnsigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/search?key=" + AppConstants.API_KEY)
    Call<SearchResult> getSearchResult(@Body String searchQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/search?key=" + AppConstants.API_KEY)
    Call<SearchResultContinuation> continueSearchResult(@Body String searchQuery);
}
