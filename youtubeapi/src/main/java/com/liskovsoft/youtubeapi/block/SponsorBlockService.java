package com.liskovsoft.youtubeapi.block;

import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.youtubeapi.common.helpers.AppHelper;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

import java.util.Arrays;
import java.util.List;

public class SponsorBlockService {
    private static SponsorBlockService sInstance;
    private final SponsorBlockManager mSponsorBlockManager;

    private SponsorBlockService() {
        mSponsorBlockManager = RetrofitHelper.withJsonPath(SponsorBlockManager.class);
    }

    public static SponsorBlockService instance() {
        if (sInstance == null) {
            sInstance = new SponsorBlockService();
        }

        return sInstance;
    }

    public SegmentList getSegmentList(String videoId) {
        Call<SegmentList> wrapper = mSponsorBlockManager.getSegments(videoId);

        return RetrofitHelper.get(wrapper);
    }

    public SegmentList getSegmentList(String videoId, String... categories) {
        return getSegmentList(videoId, Arrays.asList(categories));
    }

    public SegmentList getSegmentList(String videoId, List<String> categories) {
        Call<SegmentList> wrapper = mSponsorBlockManager.getSegments(videoId, AppHelper.toJsonArrayString(categories));

        return RetrofitHelper.get(wrapper);
    }
}
