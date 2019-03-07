package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SearchManager {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/search")
    Call<SearchResult> getSearchResult(@Query("key") String key, @Body String jsonData);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/search")
    Call<NextSearchResult> getNextPage(@Query("key") String key, @Body String jsonData);
}
