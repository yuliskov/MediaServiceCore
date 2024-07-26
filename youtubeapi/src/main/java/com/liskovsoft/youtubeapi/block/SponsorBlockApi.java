package com.liskovsoft.youtubeapi.block;

import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPathClass;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

@JsonPathClass
public interface SponsorBlockApi {
    @GET(SponsorBlockApiHelper.SEGMENTS_URL)
    Call<SegmentList> getSegments(@Query("videoID") String videoId);

    @GET(SponsorBlockApiHelper.SEGMENTS_URL)
    Call<SegmentList> getSegments(@Query("videoID") String videoId, @Query(value = "categories", encoded = true) String categoriesJsonArray);

    @GET(SponsorBlockApiHelper.SEGMENTS_URL2)
    Call<SegmentList> getSegments2(@Query("videoID") String videoId);

    @GET(SponsorBlockApiHelper.SEGMENTS_URL2)
    Call<SegmentList> getSegments2(@Query("videoID") String videoId, @Query(value = "categories", encoded = true) String categoriesJsonArray);
}
