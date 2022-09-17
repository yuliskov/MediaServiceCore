package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.videoinfo.models.DashInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DashInfoApi {
    @GET()
    Call<DashInfo> getDashInfo(@Url String url);
}
