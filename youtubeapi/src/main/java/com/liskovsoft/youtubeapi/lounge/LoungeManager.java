package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.lounge.models.BindResult;
import com.liskovsoft.youtubeapi.lounge.models.LoungeTokenResult;
import com.liskovsoft.youtubeapi.lounge.models.ScreenIdResult;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoungeManager {
    @GET("https://www.youtube.com/api/lounge/pairing/generate_screen_id")
    Call<ScreenIdResult> createScreenId();

    /**
     * <a href="https://square.github.io/retrofit/2.x/retrofit/index.html?retrofit2/http/Field.html">Field</a><br/>
     * <a href="https://square.github.io/retrofit/2.x/retrofit/retrofit2/http/FieldMap.html">FieldMap</a>
     */
    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/pairing/get_lounge_token_batch")
    Call<LoungeTokenResult> getLoungeToken(@Field("screen_ids") String... screenIds);

    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/bc/bind?" +
            "device=LOUNGE_SCREEN&theme=cl&capabilities=dsp%2Cmic%2Cdpa&mdxVersion=2&VER=8&v=2&t=1&app=lb-v4" +
            "&id=" + LoungeManagerParams.SCREEN_UID +
            "&RID=" + LoungeManagerParams.RID +
            "&AID=" + LoungeManagerParams.AID +
            "&zx=" + LoungeManagerParams.ZX)
    Call<BindResult> bind(@Query("name") String screenName, @Query("loungeIdToken") String loungeToken, @Field("count") String count);
}
