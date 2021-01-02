package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.lounge.models.BindData;
import com.liskovsoft.youtubeapi.lounge.models.PairingCode;
import com.liskovsoft.youtubeapi.lounge.models.ScreenId;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BindManager {
    @GET("https://www.youtube.com/api/lounge/pairing/generate_screen_id")
    Call<ScreenId> createScreenId();

    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/bc/bind?" +
            "device=LOUNGE_SCREEN&theme=cl&capabilities=dsp%2Cmic%2Cdpa&mdxVersion=2&VER=8&v=2&t=1" +
            "&app=" + BindManagerParams.APP +
            "&id=" + BindManagerParams.SCREEN_UID +
            "&RID=" + BindManagerParams.RID +
            "&AID=" + BindManagerParams.AID +
            "&zx=" + BindManagerParams.ZX)
    Call<BindData> bind(@Query("name") String screenName, @Query("loungeIdToken") String loungeToken, @Field("count") String count);

    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/pairing/get_pairing_code?ctx=pair")
    Call<PairingCode> getPairingCode(@Field("access_type") String accessType,
                                     @Field("app") String app,
                                     @Field("lounge_token") String loungeToken,
                                     @Field("screen_id") String screenId,
                                     @Field("screen_name") String screenName);
}
