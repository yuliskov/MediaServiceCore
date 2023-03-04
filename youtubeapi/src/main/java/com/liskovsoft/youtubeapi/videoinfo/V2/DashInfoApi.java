package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoUrl;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoContent;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DashInfoApi {
    @GET()
    Call<DashInfoUrl> getDashInfoUrl(@Url String url);

    @GET()
    Call<DashInfoContent> getDashInfoContent(@Url String url);
    
    @GET()
    Call<Void> getDashInfoHeaders(@Url String url);
}
