package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.lounge.models.info.LoungePlaylist;
import com.liskovsoft.youtubeapi.lounge.models.info.ScreenList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InfoManager {
    /**
     * <a href="https://square.github.io/retrofit/2.x/retrofit/index.html?retrofit2/http/Field.html">Field</a><br/>
     * <a href="https://square.github.io/retrofit/2.x/retrofit/retrofit2/http/FieldMap.html">FieldMap</a>
     */
    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/pairing/get_lounge_token_batch")
    Call<ScreenList> getScreenInfo(@Field("screen_ids") String... screenIds);

    @GET("https://www.youtube.com/list_ajax?style=json&action_get_list=1")
    Call<LoungePlaylist> getPlaylistInfo(@Query("list") String playlistId);
}
