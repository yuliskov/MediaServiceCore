package com.liskovsoft.youtubeapi.auth.V1;

import com.liskovsoft.googlecommon.common.models.auth.AccessToken;
import com.liskovsoft.googlecommon.common.models.auth.UserCode;
import com.liskovsoft.googlecommon.common.models.auth.info.AccountsList;
import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPath;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@WithJsonPath
public interface AuthApi {
    @FormUrlEncoded
    @POST("https://www.youtube.com/o/oauth2/device/code")
    Call<UserCode> getUserCode(@Field("client_id") String clientId,
                               @Field("scope") String scope);

    @FormUrlEncoded
    @POST("https://www.youtube.com/o/oauth2/token")
    Call<AccessToken> getAccessToken(@Field("code") String deviceCode,
                                     @Field("client_id") String clientId,
                                     @Field("client_secret") String clientSecret,
                                     @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("https://www.youtube.com/o/oauth2/token")
    Call<AccessToken> updateAccessToken(@Field("refresh_token") String refreshToken,
                                        @Field("client_id") String clientId,
                                        @Field("client_secret") String clientSecret,
                                        @Field("grant_type") String grantType);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("https://www.youtube.com/o/oauth2/token")
    Call<AccessToken> updateAccessToken(@Body RequestBody rawBody);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/account/accounts_list")
    Call<AccountsList> getAccountsList(@Body String authQuery, @Header("Authorization") String auth);
}
