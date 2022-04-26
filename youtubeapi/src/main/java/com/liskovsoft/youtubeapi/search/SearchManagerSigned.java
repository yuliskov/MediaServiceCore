package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchTags;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Example url: https://www.youtube.com/youtubei/v1/search
 */
public interface SearchManagerSigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/search")
    Call<SearchResult> getSearchResult(@Body String searchQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/search")
    Call<SearchResult> getSearchResult(@Body String searchQuery, @Header("Authorization") String auth, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/search")
    Call<SearchResultContinuation> continueSearchResult(@Body String searchQuery, @Header("Authorization") String auth);

    @GET("https://clients1.google.com/complete/search?client=youtube-lr&ds=yt&xhr=t&oe=utf-8&hl=en&gl=us")
    Call<SearchTags> getSearchTags(@Query("q") String searchQuery, @Query("tok") String suggestToken, @Header("Authorization") String auth);

    @GET("https://clients1.google.com/complete/search?client=youtube-lr&ds=yt&xhr=t&oe=utf-8")
    Call<SearchTags> getSearchTags(@Query("q") String searchQuery,
                                   @Query("tok") String suggestToken,
                                   @Query("gl") String country,
                                   @Query("hl") String language,
                                   @Header("Authorization") String auth);

    @GET("https://clients1.google.com/complete/search?client=youtube-lr&ds=yt&xhr=t&oe=utf-8")
    Call<SearchTags> getSearchTags(@Query("q") String searchQuery,
                                   @Query("tok") String suggestToken,
                                   @Query("gl") String country,
                                   @Query("hl") String language,
                                   @Header("Authorization") String auth,
                                   @Header("X-Goog-Visitor-Id") String visitorId);
}
