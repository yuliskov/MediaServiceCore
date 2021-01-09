package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.lounge.models.CommandInfos;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommandManager {
    @FormUrlEncoded
    @POST("https://www.youtube.com/api/lounge/bc/bind?" +
            "device=LOUNGE_SCREEN&theme=cl&capabilities=dsp%2Cmic%2Cdpa&mdxVersion=2&VER=8&v=2&t=1" +
            "&app=" + BindManagerParams.APP +
            "&id=" + BindManagerParams.SCREEN_UID +
            "&RID=" + BindManagerParams.RID +
            "&AID=" + BindManagerParams.AID +
            "&zx=" + BindManagerParams.ZX)
    Call<CommandInfos> bind1(@Query("name") String screenName,
                             @Query("loungeIdToken") String loungeToken,
                             @Field("count") int count);

    @GET("https://www.youtube.com/api/lounge/bc/bind?" +
            "device=LOUNGE_SCREEN&theme=cl&capabilities=dsp%2Cmic%2Cdpa&mdxVersion=2&VER=8&v=2&t=1" +
            "&RID=rpc&CI=0" +
            "&app=" + BindManagerParams.APP +
            "&id=" + BindManagerParams.SCREEN_UID +
            "&AID=" + BindManagerParams.AID +
            "&zx=" + BindManagerParams.ZX)
    Call<CommandInfos> bind2(@Query("name") String screenName,
                             @Query("loungeIdToken") String loungeToken,
                             @Query("SID") String screenId, // value from bind1
                             @Query("gsessionid") String sessionId); // value from bind1
}
