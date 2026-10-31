package com.liskovsoft.youtubeapi.block;

import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.IOException;
import java.util.Set;

public class SponsorBlockService {
    private static SponsorBlockService sInstance;
    private final SponsorBlockApi mSponsorBlockApi;

    private SponsorBlockService() {
        this(RetrofitHelper.create(SponsorBlockApi.class));
    }

    SponsorBlockService(SponsorBlockApi sponsorBlockApi) {
        mSponsorBlockApi = sponsorBlockApi;
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

        return getSegmentList(wrapper);
    }

    public SegmentList getSegmentList(String videoId, Set<String> categories) {
        return categories != null && categories.size() > 0 ? getSegmentListInt(videoId, categories) : getSegmentList(videoId);
    }

    private SegmentList getSegmentListInt(String videoId, Set<String> categories) {
        Call<SegmentList> wrapper = isAltServerEnabled() ?
                mSponsorBlockApi.getSegments2(videoId, ServiceHelper.toJsonArrayString(categories)) :
                mSponsorBlockApi.getSegments(videoId, ServiceHelper.toJsonArrayString(categories));

        return getSegmentList(wrapper);
    }

    private SegmentList getSegmentList(Call<SegmentList> wrapper) {
        try {
            Response<SegmentList> response = wrapper.execute();
            if (response.code() == 404) {
                return SegmentList.empty();
            }
            if (!response.isSuccessful()) {
                throw new HttpException(response);
            }
            if (response.body() == null) {
                throw new IllegalStateException("SponsorBlock response has no segment data");
            }
            return response.body();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to fetch SponsorBlock segments", e);
        }
    }

    private boolean isAltServerEnabled() {
        return GlobalPreferences.isInitialized() && GlobalPreferences.sInstance.isContentBlockAltServerEnabled();
    }
}
