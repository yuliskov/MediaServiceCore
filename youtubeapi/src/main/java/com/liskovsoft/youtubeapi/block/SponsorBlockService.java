package com.liskovsoft.youtubeapi.block;

import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

import java.util.Set;

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

    public SegmentList getSegmentList(String videoId, Set<String> categories) {
        return categories != null && categories.size() > 0 ? getSegmentListInt(videoId, categories) : getSegmentList(videoId);
    }

    private SegmentList getSegmentListInt(String videoId, Set<String> categories) {
        Call<SegmentList> wrapper = mSponsorBlockManager.getSegments(videoId, ServiceHelper.toJsonArrayString(categories));

        return RetrofitHelper.get(wrapper);
    }
}
