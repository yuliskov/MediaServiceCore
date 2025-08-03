package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPath;
import com.liskovsoft.youtubeapi.lounge.models.info.PairingCodeV2;
import com.liskovsoft.youtubeapi.lounge.models.info.PlaylistInfo;
import com.liskovsoft.youtubeapi.lounge.models.info.TokenInfoList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@WithJsonPath
public interface InfoManager {
    /**
     * <a href="https://square.github.io/retrofit/2.x/retrofit/index.html?retrofit2/http/Field.html">Field</a><br/>
     * <a href="https://square.github.io/retrofit/2.x/retrofit/retrofit2/http/FieldMap.html">FieldMap</a>
     */
    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/pairing/get_lounge_token_batch")
    Call<TokenInfoList> getTokenInfo(@Field("screen_ids") String... screenIds);

    @GET("https://www.youtube.com/list_ajax?style=json&action_get_list=1")
    Call<PlaylistInfo> getPlaylistInfo(@Query("list") String playlistId);

    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/pairing/get_pairing_code?ctx=pair")
    Call<PairingCodeV2> getPairingCodeV2(@Field("lounge_token") String loungeToken,
                                         @Field("screen_id") String screenId,
                                         @Field("screen_name") String screenName,
                                         @Field("access_type") String accessType,
                                         @Field("app") String app,
                                         @Field("device_id") String deviceId,
                                         @Field("qr") String qr);
}
