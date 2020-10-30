package com.liskovsoft.youtubeapi.auth.V2;

import com.liskovsoft.youtubeapi.auth.models.auth.AccessToken;
import com.liskovsoft.youtubeapi.auth.models.auth.RefreshToken;
import com.liskovsoft.youtubeapi.auth.models.auth.UserCode;
import com.liskovsoft.youtubeapi.auth.models.info.AccountsList;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthManager {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/o/oauth2/device/code")
    Call<UserCode> getUserCode(@Body String authQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/o/oauth2/token")
    Call<RefreshToken> getRefreshToken(@Body String authQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/o/oauth2/token")
    Call<AccessToken> getAccessToken(@Body String authQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/account/accounts_list")
    Call<AccountsList> getAccountsList(@Body String authQuery, @Header("Authorization") String auth);
}
