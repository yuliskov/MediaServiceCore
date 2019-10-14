package com.liskovsoft.youtubeapi.content_old;

import com.liskovsoft.youtubeapi.content_old.models.RootContent;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ContentManager {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse?key=AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8")
    Call<RootContent> getRootContent(@Body String contentQuery);
}
