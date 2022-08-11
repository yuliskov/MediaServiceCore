package com.liskovsoft.youtubeapi.pageinfo.V1;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.pageinfo.models.PageInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PageInfoManagerUnsigned {
    @GET("https://www.youtube.com/watch?bpctr=9999999999&has_verified=1")
    Call<PageInfo> getPageInfo(@Query("v") String videoId, @Query("hl") String lang);

    /**
     * Good for live translations
     */
    @GET("https://www.youtube.com/watch?bpctr=9999999999&has_verified=1")
    Call<PageInfo> getPageInfoHls(@Query("v") String videoId, @Query("hl") String lang);

    /**
     * Good for age restricted pages
     */
    @GET("https://www.youtube.com/watch?has_verified=1")
    Call<PageInfo> getPageInfoRestricted(@Query("v") String videoId, @Query("hl") String lang);
}
