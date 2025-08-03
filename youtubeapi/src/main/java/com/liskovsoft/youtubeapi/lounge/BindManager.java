package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.googlecommon.common.converters.regexp.WithRegExp;
import com.liskovsoft.youtubeapi.lounge.models.bind.PairingCode;
import com.liskovsoft.youtubeapi.lounge.models.bind.ScreenId;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

@WithRegExp
public interface BindManager {
    @GET("https://www.youtube.com/api/lounge/pairing/generate_screen_id")
    Call<ScreenId> createScreenId();

    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/pairing/get_pairing_code?ctx=pair")
    Call<PairingCode> getPairingCode(@Field("lounge_token") String loungeToken,
                                     @Field("screen_id") String screenId,
                                     @Field("screen_name") String screenName,
                                     @Field("access_type") String accessType,
                                     @Field("app") String app);
}
