package com.liskovsoft.youtubeapi.suggest;

import com.liskovsoft.youtubeapi.suggest.models.NextSuggestResult;
import com.liskovsoft.youtubeapi.suggest.models.SuggestResult;
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
    @POST("https://www.youtube.com/youtubei/v1/next")
    Call<SuggestResult> getSuggestResult(@Body String suggestQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    Call<NextSuggestResult> getNextSuggestResult(@Body String suggestQuery, @Header("Authorization") String auth);
}
