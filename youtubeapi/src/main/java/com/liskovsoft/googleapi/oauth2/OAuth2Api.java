package com.liskovsoft.googleapi.oauth2;

import com.liskovsoft.googlecommon.common.models.auth.AccessToken;
import com.liskovsoft.googlecommon.common.models.auth.UserCode;
import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPath;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * https://developers.google.com/identity/protocols/oauth2/limited-input-device
 */
@WithJsonPath
interface OAuth2Api {
    @FormUrlEncoded
    @POST("https://oauth2.googleapis.com/device/code")
    Call<UserCode> getUserCode(@Field("client_id") String clientId, @Field("scope") String scope);

    /**
     * Poll Google's authorization server
     */
    @FormUrlEncoded
    @POST("https://oauth2.googleapis.com/token")
    Call<AccessToken> getAccessToken(@Field("client_id") String clientId, @Field("client_secret") String clientSecret,
                                     @Field("device_code") String deviceCode, @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("https://oauth2.googleapis.com/token")
    Call<AccessToken> updateAccessToken(@Field("client_id") String clientId, @Field("client_secret") String clientSecret,
                                        @Field("grant_type") String grantType, @Field("refresh_token") String refreshToken);
}
