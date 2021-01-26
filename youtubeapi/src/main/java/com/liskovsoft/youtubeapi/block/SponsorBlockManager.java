package com.liskovsoft.youtubeapi.block;

import com.liskovsoft.youtubeapi.block.data.SegmentList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SponsorBlockManager {
    @GET(SponsorBlockParams.SEGMENTS_URL)
    Call<SegmentList> getSegments(@Query("videoID") String videoId);

    @GET(SponsorBlockParams.SEGMENTS_URL)
    Call<SegmentList> getSegments(@Query("videoID") String videoId, @Query(value = "categories", encoded = true) String categoriesJsonArray);
}
