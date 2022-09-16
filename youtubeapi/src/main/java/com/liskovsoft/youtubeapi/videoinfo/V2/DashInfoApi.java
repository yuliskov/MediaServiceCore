package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.videoinfo.models.DashInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DashInfoApi {
    @GET("{url}")
    Call<DashInfo> getDashInfo(@Path("url") String url);
}
