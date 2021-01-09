package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.lounge.models.commands.CommandInfos;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommandManager {
    @FormUrlEncoded
    @POST(BindManagerParams.BIND_DATA_URL)
    Call<CommandInfos> getBindData(@Query("name") String screenName,
                                   @Query("loungeIdToken") String loungeToken,
                                   @Field("count") int count);
}
