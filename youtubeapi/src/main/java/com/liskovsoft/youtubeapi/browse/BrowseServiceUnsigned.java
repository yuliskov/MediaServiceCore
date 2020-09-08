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
    private final BrowseManagerUnsigned mBrowseManagerUnsigned;
    private String mVisitorData;
    private String mNextHomeTabsKey;

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
        Call<TabbedBrowseResult> wrapper = mBrowseManagerUnsigned.getTabbedBrowseResult(query);

        TabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private TabbedBrowseResultContinuation getNextTabbedResult(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getNextBrowseQuery(nextKey);

        Call<TabbedBrowseResultContinuation> wrapper = mBrowseManagerUnsigned.getContinueTabbedBrowseResult(query, visitorData);

        TabbedBrowseResultContinuation browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private BrowseResultContinuation getNextResult(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getNextBrowseQuery(nextKey);

        Call<BrowseResultContinuation> wrapper =
                mBrowseManagerUnsigned.getContinueBrowseResult(query, visitorData);

        BrowseResultContinuation browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private BrowseTab findHomeTab(TabbedBrowseResult homeTabs) {
        BrowseTab result = null;

        if (homeTabs != null) {
            result = homeTabs.getBrowseTabs().get(0);
        } else {
            Log.e(TAG, "findRootTab: tabs are empty");
        }

        return result;
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

    public TabbedBrowseResultContinuation continueTab(String nextPageKey) {
        TabbedBrowseResultContinuation nextHomeTabs = null;

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
