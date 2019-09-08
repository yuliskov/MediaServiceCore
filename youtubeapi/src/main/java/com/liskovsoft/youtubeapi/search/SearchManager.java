package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Example url: https://www.youtube.com/youtubei/v1/search?key=AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8
 */
public interface SearchManager {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/search")
    Call<SearchResult> getSearchResult(@Query("key") String key, @Body String searchQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/search")
    Call<NextSearchResult> getNextSearchResult(@Query("key") String key, @Body String searchQuery);
}
