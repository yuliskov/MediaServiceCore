package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.googlecommon.common.converters.regexp.WithRegExp;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoContent;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoUrl;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

@WithRegExp
public interface DashInfoApi {
    @GET
    Call<DashInfoUrl> getDashInfoUrl(@Url String url);

    @GET
    Call<DashInfoContent> getDashInfoContent(@Url String url);
}
