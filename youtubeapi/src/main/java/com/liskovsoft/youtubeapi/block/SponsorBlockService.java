package com.liskovsoft.youtubeapi.block;

import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
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
        Call<SegmentList> wrapper =
                isAltServerEnabled() ? mSponsorBlockManager.getSegments2(videoId) : mSponsorBlockManager.getSegments(videoId);

        return RetrofitHelper.get(wrapper);
    }

    public SegmentList getSegmentList(String videoId, Set<String> categories) {
        return categories != null && categories.size() > 0 ? getSegmentListInt(videoId, categories) : getSegmentList(videoId);
    }

    private SegmentList getSegmentListInt(String videoId, Set<String> categories) {
        Call<SegmentList> wrapper = isAltServerEnabled() ?
                mSponsorBlockManager.getSegments2(videoId, ServiceHelper.toJsonArrayString(categories)) :
                mSponsorBlockManager.getSegments(videoId, ServiceHelper.toJsonArrayString(categories));

        return RetrofitHelper.get(wrapper);
    }

    private boolean isAltServerEnabled() {
        return GlobalPreferences.isInitialized() && GlobalPreferences.sInstance.isContentBlockAltServerEnabled();
    }
}
