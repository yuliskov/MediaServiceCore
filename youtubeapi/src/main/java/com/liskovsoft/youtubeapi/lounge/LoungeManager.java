package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.lounge.models.LoungeTokenBatch;
import com.liskovsoft.youtubeapi.lounge.models.ScreenId;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoungeManager {
    @GET("https://www.youtube.com/api/lounge/pairing/generate_screen_id")
    Call<ScreenId> getScreenId();

    /**
     * <a href="https://square.github.io/retrofit/2.x/retrofit/index.html?retrofit2/http/Field.html">Field</a><br/>
     * <a href="https://square.github.io/retrofit/2.x/retrofit/retrofit2/http/FieldMap.html">FieldMap</a>
     */
    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/pairing/get_lounge_token_batch")
    Call<LoungeTokenBatch> getLoungeTokenBatch(@Field("screen_ids") String... screenIds);
}
