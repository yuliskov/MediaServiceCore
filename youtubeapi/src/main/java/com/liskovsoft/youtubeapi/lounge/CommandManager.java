package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPathSkip;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.Map;

@WithJsonPathSkip
public interface CommandManager {
    @FormUrlEncoded
    @POST(BindParams.BIND_DATA_URL)
    Call<CommandList> getSessionData(@Query("name") String screenName,
                                     @Query("id") String deviceId,
                                     @Query("loungeIdToken") String loungeToken,
                                     @Field("count") int count);

    @FormUrlEncoded
    @POST(BindParams.BIND_DATA_URL)
    Call<Void> postCommand(@Query("name") String screenName,
                           @Query("id") String deviceId,
                           @Query("loungeIdToken") String loungeToken,
                           @Query("SID") String sessionId,
                           @Query("gsessionid") String gSessionId,
                           @FieldMap Map<String, String> fields);
}
