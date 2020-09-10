package com.liskovsoft.youtubeapi.browse.ver2;

import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuationResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTabContinuationResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTabResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For signed users!
 */
public interface BrowseManagerSigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<GridTabResult> getGridTabResult(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<GridTabContinuationResult> continueGridTabResult(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<RowsTabResult> getRowsTabResult(@Body String browseQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<RowsTabContinuationResult> continueRowsTabResult(@Body String browseQuery, @Header("Authorization") String auth);
}
