package com.liskovsoft.youtubeapi.block;

import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;
import retrofit2.Call;

import java.util.Set;

public class SponsorBlockService {
    private static SponsorBlockService sInstance;
    private final SponsorBlockApi mSponsorBlockApi;

    private SponsorBlockService() {
        mSponsorBlockApi = RetrofitHelper.create(SponsorBlockApi.class);
    }

    public static SponsorBlockService instance() {
        if (sInstance == null) {
            sInstance = new SponsorBlockService();
        }

        return sInstance;
    }

    public SegmentList getSegmentList(String videoId) {
        Call<SegmentList> wrapper =
                isAltServerEnabled() ? mSponsorBlockApi.getSegments2(videoId) : mSponsorBlockApi.getSegments(videoId);

        return RetrofitHelper.get(wrapper);
    }

    public SegmentList getSegmentList(String videoId, Set<String> categories) {
        return categories != null && categories.size() > 0 ? getSegmentListInt(videoId, categories) : getSegmentList(videoId);
    }

    private SegmentList getSegmentListInt(String videoId, Set<String> categories) {
        Call<SegmentList> wrapper = isAltServerEnabled() ?
                mSponsorBlockApi.getSegments2(videoId, ServiceHelper.toJsonArrayString(categories)) :
                mSponsorBlockApi.getSegments(videoId, ServiceHelper.toJsonArrayString(categories));

        return RetrofitHelper.get(wrapper);
    }

    private boolean isAltServerEnabled() {
        return GlobalPreferences.isInitialized() && GlobalPreferences.sInstance.isContentBlockAltServerEnabled();
    }
}
