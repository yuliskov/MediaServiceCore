package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class BrowseServiceUnsigned {
    private static final String TAG = BrowseServiceUnsigned.class.getSimpleName();
    private static BrowseServiceUnsigned sInstance;
    private final BrowseManagerUnsigned mBrowseManager;
    private String mVisitorData;
    private String mNextHomeTabsKey;

    private BrowseServiceUnsigned() {
        mBrowseManager = RetrofitHelper.withJsonPath(BrowseManagerUnsigned.class);
    }

    public static BrowseServiceUnsigned instance() {
        if (sInstance == null) {
            sInstance = new BrowseServiceUnsigned();
        }

        return sInstance;
    }

    public List<BrowseSection> getHomeSections() {
        TabbedBrowseResult homeTabs = getTabbedResult(BrowseManagerParams.getHomeQuery());

        if (homeTabs == null) {
            Log.e(TAG, "Home tabs are empty");
            return new ArrayList<>();
        }

        mVisitorData = homeTabs.getVisitorData();
        mNextHomeTabsKey = findHomeTab(homeTabs).getNextPageKey();

        return findHomeTab(homeTabs).getSections();
    }

    public List<BrowseSection> getNextHomeSections() {
        TabbedBrowseResultContinuation nextHomeTabs = null;

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

    public BrowseResultContinuation continueSection(String nextPageKey) {
        return getNextResult(nextPageKey, mVisitorData);
    }

    private TabbedBrowseResult getTabbedResult(String query) {
        Call<TabbedBrowseResult> wrapper = mBrowseManager.getTabbedBrowseResult(query);

        TabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private TabbedBrowseResultContinuation getNextTabbedResult(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getNextBrowseQuery(nextKey);

        Call<TabbedBrowseResultContinuation> wrapper = mBrowseManager.getContinueTabbedBrowseResult(query, visitorData);

        TabbedBrowseResultContinuation browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private BrowseResultContinuation getNextResult(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getNextBrowseQuery(nextKey);

        Call<BrowseResultContinuation> wrapper =
                mBrowseManager.getContinueBrowseResult(query, visitorData);

        BrowseResultContinuation browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private BrowseTab findHomeTab(TabbedBrowseResult homeTabs) {
        return homeTabs.getBrowseTabs().get(0);
    }
}
