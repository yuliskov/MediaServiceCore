package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.lounge.models.StateResult;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandInfos;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommandManager {
    @FormUrlEncoded
    @POST(BindParams.BIND_DATA_URL)
    Call<CommandInfos> getFistBindData(@Query("name") String screenName,
                                       @Query("loungeIdToken") String loungeToken,
                                       @Field("count") int count);

    @FormUrlEncoded
    @POST(BindParams.BIND_DATA_URL)
    Call<StateResult> postState(@Query("name") String screenName,
                                @Query("loungeIdToken") String loungeToken,
                                @Field("count") int count,
                                @Field("req0__sc") String stateCommand,
                                @Field("req0_state") String stateType,
                                @Field("req0_videoId") String videoId,
                                @Field("req0_listId") String playlistId,
                                @Field("req0_currentTime") String position,
                                @Field("req0_duration") String duration,
                                @Field("req0_loadedTime") String loadedTime,
                                @Field("req0_seekableStartTime") String startTime,
                                @Field("req0_seekableEndTime") String endTime,
                                @Field("req0_cpn") String cpn);
}
