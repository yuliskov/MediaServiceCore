package com.liskovsoft.youtubeapi.browse.old;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.old.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.old.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.old.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.browse.old.models.sections.RowsTabContinuation;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

public class BrowseServiceUnsigned {
    private static final String TAG = BrowseServiceUnsigned.class.getSimpleName();
    private static BrowseServiceUnsigned sInstance;
    private final BrowseManagerUnsigned mBrowseManagerUnsigned;
    private String mVisitorData;

    private BrowseServiceUnsigned() {
        mBrowseManagerUnsigned = RetrofitHelper.withJsonPath(BrowseManagerUnsigned.class);
    }

    public static BrowseServiceUnsigned instance() {
        if (sInstance == null) {
            sInstance = new BrowseServiceUnsigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    public BrowseTab getHome() {
        return getTab(BrowseManagerParams.getHomeQuery());
    }

    public BrowseTab getGaming() {
        return getTab(BrowseManagerParams.getGamingQuery());
    }

    public BrowseTab getNews() {
        return getTab(BrowseManagerParams.getNewsQuery());
    }

    public BrowseTab getMusic() {
        return getTab(BrowseManagerParams.getMusicQuery());
    }

    public BrowseResultContinuation continueSection(String nextPageKey) {
        return getNextResult(nextPageKey, mVisitorData);
    }

    private TabbedBrowseResult getTabbedResult(String query) {
        Call<TabbedBrowseResult> wrapper = mBrowseManagerUnsigned.getTabbedBrowseResult(query);

        return RetrofitHelper.get(wrapper);
    }

    private RowsTabContinuation getNextTabbedResult(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getNextBrowseQuery(nextKey);

        Call<RowsTabContinuation> wrapper = mBrowseManagerUnsigned.getContinueTabbedBrowseResult(query, visitorData);

        return RetrofitHelper.get(wrapper);
    }

    private BrowseResultContinuation getNextResult(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getNextBrowseQuery(nextKey);

        Call<BrowseResultContinuation> wrapper =
                mBrowseManagerUnsigned.getContinueBrowseResult(query, visitorData);

        return RetrofitHelper.get(wrapper);
    }

    private BrowseTab getTab(String query) {
        TabbedBrowseResult tabs = getTabbedResult(query);

        if (tabs == null) {
            Log.e(TAG, "getTabs: tabs result is empty");
            return null;
        }

        mVisitorData = tabs.getVisitorData();

        return firstNotEmpty(tabs);
    }

    private BrowseTab firstNotEmpty(TabbedBrowseResult tabs) {
        BrowseTab result = null;

        if (tabs.getBrowseTabs() != null) {
            // find first not empty tab
            for (BrowseTab browseTab : tabs.getBrowseTabs()) {
                if (browseTab.getSections() != null) {
                    result = browseTab;
                    break;
                }
            }
        } else {
            Log.e(TAG, "firstNotEmpty: tabs are empty");
        }

        return result;
    }

    public RowsTabContinuation continueTab(String nextPageKey) {
        RowsTabContinuation nextHomeTabs = null;

        if (mVisitorData == null) {
            Log.e(TAG, "continueTab: visitor data is null");
        }

        if (nextPageKey != null) {
            nextHomeTabs = getNextTabbedResult(nextPageKey, mVisitorData);
        }

        if (nextHomeTabs == null) {
            Log.e(TAG, "NextHomeTabs are empty");
            return null;
        }

        return nextHomeTabs;
    }
}
