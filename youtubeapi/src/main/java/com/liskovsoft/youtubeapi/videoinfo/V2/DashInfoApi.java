package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoUrl;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoFormat;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DashInfoApi {
    @GET()
    Call<DashInfoUrl> getDashInfoUrl(@Url String url);

    @GET()
    Call<DashInfoFormat> getDashInfoFormat(@Url String url);

    @GET()
    Call<Void> getDashInfoFormat2(@Url String url);
}
