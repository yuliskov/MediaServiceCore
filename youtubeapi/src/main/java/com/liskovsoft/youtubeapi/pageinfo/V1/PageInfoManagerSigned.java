package com.liskovsoft.youtubeapi.pageinfo.V1;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.pageinfo.models.PageInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * NOTE: html5=1 is required. Otherwise you'll get 404 error "This is not a P3P policy!"<br/>
 * Unlock Gemini Man 4K: https://www.youtube.com/get_page_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv<br/>
 * Unlock age restricted pages: https://www.youtube.com/get_page_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv
 */
public interface PageInfoManagerSigned {
    @GET("https://www.youtube.com/watch?bpctr=9999999999&has_verified=1")
    Call<PageInfo> getPageInfo(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang);

    /**
     * Unlock live hls streams
     */
    @GET("https://www.youtube.com/watch?bpctr=9999999999&has_verified=1")
    Call<PageInfo> getPageInfoHls(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang);

    /**
     * Unlock age restricted pages
     */
    @GET("https://www.youtube.com/watch?bpctr=9999999999&has_verified=1")
    Call<PageInfo> getPageInfoRestricted(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang);

    /**
     * Unlock personal pages: <code>c=TVHTML5&cver=7.20201103.00.00</code><br/>
     */
    @GET("https://www.youtube.com/watch?bpctr=9999999999&has_verified=1")
    Call<PageInfo> getPageInfoRegular(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang);
}
