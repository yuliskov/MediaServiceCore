package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.lounge.models.ScreenInfos;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ScreenManager {
    /**
     * <a href="https://square.github.io/retrofit/2.x/retrofit/index.html?retrofit2/http/Field.html">Field</a><br/>
     * <a href="https://square.github.io/retrofit/2.x/retrofit/retrofit2/http/FieldMap.html">FieldMap</a>
     */
    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/pairing/get_lounge_token_batch")
    Call<ScreenInfos> getScreenInfos(@Field("screen_ids") String... screenIds);
}
