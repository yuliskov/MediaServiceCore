package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.NextTabbedBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class BrowseService {
    private static final String TAG = BrowseService.class.getSimpleName();
    private static BrowseService sInstance;
    private final BrowseManager mBrowseManager;
    private String mVisitorData;
    private String mNextHomeTabsKey;

    private BrowseService() {
        mBrowseManager = RetrofitHelper.withJsonPath(BrowseManager.class);
    }

    public static BrowseService instance() {
        if (sInstance == null) {
            sInstance = new BrowseService();
        }

        return sInstance;
    }

    public List<BrowseSection> getHomeSections() {
        TabbedBrowseResult homeTabs = getTabbedResult(BrowseParams.getHomeQuery());

        if (homeTabs == null) {
            Log.e(TAG, "HomeTabs are empty");
            return new ArrayList<>();
        }

        mVisitorData = homeTabs.getVisitorData();
        mNextHomeTabsKey = findHomeTab(homeTabs).getNextPageKey();

        return findHomeTab(homeTabs).getSections();
    }

    public List<BrowseSection> getNextHomeSections() {
        NextTabbedBrowseResult nextHomeTabs = null;

        if (mNextHomeTabsKey != null) {
            nextHomeTabs = getNextTabbedResult(mNextHomeTabsKey, mVisitorData);

            if (nextHomeTabs != null) {
                mNextHomeTabsKey = nextHomeTabs.getNextPageKey();
            } else {
                mNextHomeTabsKey = null;
            }
        }

        if (nextHomeTabs == null) {
            Log.e(TAG, "NextHomeTabs are empty");
            return new ArrayList<>();
        }

        return nextHomeTabs.getSections();
    }

    public NextBrowseResult continueSection(String nextPageKey) {
        return getNextResult(nextPageKey, mVisitorData);
    }

    private TabbedBrowseResult getTabbedResult(String query) {
        Call<TabbedBrowseResult> wrapper = mBrowseManager.getTabbedBrowseResult(query);

        TabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private NextTabbedBrowseResult getNextTabbedResult(String nextKey, String visitorData) {
        String query = BrowseParams.getNextBrowseQuery(nextKey);

        Call<NextTabbedBrowseResult> wrapper = mBrowseManager.getNextTabbedBrowseResult(query, visitorData);

        NextTabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private NextBrowseResult getNextResult(String nextKey, String visitorData) {
        String query = BrowseParams.getNextBrowseQuery(nextKey);

        Call<NextBrowseResult> wrapper =
                mBrowseManager.getNextBrowseResult(query, visitorData);

        NextBrowseResult browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private BrowseTab findHomeTab(TabbedBrowseResult homeTabs) {
        return homeTabs.getBrowseTabs().get(0);
    }
}
