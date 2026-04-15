package com.liskovsoft.youtubeapi.search.v2;

import com.liskovsoft.googlecommon.common.converters.gson.WithGson;
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation;
import com.liskovsoft.youtubeapi.search.v2.gen.SearchResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@WithGson
public interface SearchApi {
    String SEARCH_URL = "https://www.youtube.com/youtubei/v1/search";

    @Headers("Content-Type: application/json")
    @POST(SEARCH_URL)
    Call<SearchResult> getSearchResult(@Body String searchQuery);

    @Headers("Content-Type: application/json")
    @POST(SEARCH_URL)
    Call<SearchResult> getSearchResult(@Body String searchQuery, @Header("X-Goog-Visitor-Id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST(SEARCH_URL)
    Call<WatchNextResultContinuation> continueSearchResult(@Body String searchQuery);
}
