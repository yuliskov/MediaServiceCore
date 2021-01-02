package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.lounge.models.BindResult;
import com.liskovsoft.youtubeapi.lounge.models.ScreenIdResult;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BindManager {
    @GET("https://www.youtube.com/api/lounge/pairing/generate_screen_id")
    Call<ScreenIdResult> createScreenId();

    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/bc/bind?" +
            "device=LOUNGE_SCREEN&theme=cl&capabilities=dsp%2Cmic%2Cdpa&mdxVersion=2&VER=8&v=2&t=1&app=lb-v4" +
            "&id=" + BindManagerParams.SCREEN_UID +
            "&RID=" + BindManagerParams.RID +
            "&AID=" + BindManagerParams.AID +
            "&zx=" + BindManagerParams.ZX)
    Call<BindResult> bind(@Query("name") String screenName, @Query("loungeIdToken") String loungeToken, @Field("count") String count);
}
