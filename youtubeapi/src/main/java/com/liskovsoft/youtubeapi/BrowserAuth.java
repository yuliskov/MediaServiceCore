package com.liskovsoft.youtubeapi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BrowserAuth {
    @FormUrlEncoded
    @POST("https://www.youtube.com/o/oauth2/device/code")
    Call<UserCode> getUserCode(@Field("client_id") String clientId, @Field("scope") String scope);
}
