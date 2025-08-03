package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPath;
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

@WithJsonPath
public interface SearchApi {
    String TAGS_URL = "https://clients1.google.com/complete/search?client=youtube-lr&ds=yt&xhr=t&oe=utf-8&xssi=t";
    String SEARCH_URL = "https://www.youtube.com/youtubei/v1/search";

    @Headers("Content-Type: application/json")
    @POST(SEARCH_URL)
    Call<SearchResult> getSearchResult(@Body String searchQuery);

    @Headers("Content-Type: application/json")
    @POST(SEARCH_URL)
    Call<SearchResult> getSearchResult(@Body String searchQuery, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST(SEARCH_URL)
    Call<SearchResultContinuation> continueSearchResult(@Body String searchQuery);

    @GET(TAGS_URL + "&hl=en&gl=us")
    Call<SearchTags> getSearchTags(@Query("q") String searchQuery, @Query("tok") String suggestToken);

    @GET(TAGS_URL)
    Call<SearchTags> getSearchTags(@Query("q") String searchQuery,
                                   @Query("tok") String suggestToken,
                                   @Query("gl") String country,
                                   @Query("hl") String language);

    @GET(TAGS_URL)
    Call<SearchTags> getSearchTags(@Query("q") String searchQuery,
                                   @Query("tok") String suggestToken,
                                   @Query("gl") String country,
                                   @Query("hl") String language,
                                   @Header("X-Goog-Visitor-Id") String visitorId);
}
