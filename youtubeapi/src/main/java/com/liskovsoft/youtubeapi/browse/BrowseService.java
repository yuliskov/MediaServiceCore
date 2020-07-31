package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.NextTabbedBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowseService {
    private static final String TAG = BrowseService.class.getSimpleName();
    private BrowseManager mBrowseManager;
    private String mNextPageKey;
    private TabbedBrowseResult mHomeTabs;
    private NextTabbedBrowseResult mNextHomeTabs;
    private final Map<Integer, NextBrowseResult> mNextHomeResultMap = new HashMap<>();

    public List<BrowseSection> getHomeSections() {
        mHomeTabs = getTabbedResult(BrowseParams.getHomeQuery());

        if (mHomeTabs == null) {
            Log.e(TAG, "HomeTabs are empty");
            return new ArrayList<>();
        }

        return findHomeTab().getSections();
    }

    public List<BrowseSection> getNextHomeSections() {
        if (mNextHomeTabs == null) {
            mNextHomeTabs = getNextTabbedResult(findHomeTab().getNextPageKey(), mHomeTabs.getVisitorData());
        } else {
            mNextHomeTabs = getNextTabbedResult(mNextHomeTabs.getNextPageKey(), mHomeTabs.getVisitorData());
        }

        if (mNextHomeTabs == null) {
            Log.e(TAG, "NextHomeTabs are empty");
            return new ArrayList<>();
        }

        return mNextHomeTabs.getSections();
    }

    public List<VideoItem> continueHomeSection(int sectionIndex) {
        List<VideoItem> result  = null;
        NextBrowseResult browseResult = null;

        if (mNextHomeResultMap.containsKey(sectionIndex)) {
            browseResult = getNextResult(mNextHomeResultMap.get(sectionIndex).getNextPageKey(), mHomeTabs.getVisitorData());
        } else {
            BrowseSection videoSection = VideoServiceHelper.getSection(mHomeTabs, sectionIndex);

            if (videoSection != null) {
                browseResult = getNextResult(videoSection.getNextPageKey(), mHomeTabs.getVisitorData());
            }
        }

        if (browseResult != null) {
            result = browseResult.getVideoItems();
            mNextHomeResultMap.put(sectionIndex, browseResult);
        }

        return result;
    }

    private BrowseManager getBrowseManager() {
        if (mBrowseManager == null) {
            mBrowseManager = RetrofitHelper.withJsonPath(BrowseManager.class);
        }

        return mBrowseManager;
    }

    private TabbedBrowseResult getTabbedResult(String query) {
        BrowseManager manager = getBrowseManager();

        Call<TabbedBrowseResult> wrapper = manager.getTabbedBrowseResult(query);

        TabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private NextTabbedBrowseResult getNextTabbedResult(String nextKey, String visitorData) {
        String query = BrowseParams.getNextBrowseQuery(nextKey);

        BrowseManager manager = getBrowseManager();

        Call<NextTabbedBrowseResult> wrapper = manager.getNextTabbedBrowseResult(query, visitorData);

        NextTabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private NextBrowseResult getNextResult(String nextKey, String visitorData) {
        String query = BrowseParams.getNextBrowseQuery(nextKey);

        BrowseManager manager = getBrowseManager();

        Call<NextBrowseResult> wrapper =
                manager.getNextBrowseResult(query, visitorData);

        NextBrowseResult browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private BrowseTab findHomeTab() {
        return mHomeTabs.getBrowseTabs().get(0);
    }
}
