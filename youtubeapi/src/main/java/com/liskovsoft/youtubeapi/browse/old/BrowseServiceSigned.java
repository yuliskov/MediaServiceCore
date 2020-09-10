package com.liskovsoft.youtubeapi.browse.old;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.browse.old.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.old.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.old.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.old.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.browse.old.models.sections.RowsTabContinuation;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

/**
 * For auth users only!<br/>
 * Wraps result from the {@link AuthManager} and {@link BrowseManagerSigned}
 */
public class BrowseServiceSigned {
    private static final String TAG = BrowseServiceSigned.class.getSimpleName();
    private final BrowseManagerSigned mBrowseManagerSigned;
    private static BrowseServiceSigned sInstance;

    private BrowseServiceSigned() {
        mBrowseManagerSigned = RetrofitHelper.withJsonPath(BrowseManagerSigned.class);
    }

    public static BrowseServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new BrowseServiceSigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    public BrowseResult getSubscriptions(String authorization) {
        return getSection(BrowseManagerParams.getSubscriptionsQuery(), authorization);
    }

    public BrowseResult getHistory(String authorization) {
        return getSection(BrowseManagerParams.getHistoryQuery(), authorization);
    }

    public BrowseTab getHome(String authorization) {
        return getTab(BrowseManagerParams.getHomeQuery(), authorization);
    }

    public BrowseTab getGaming(String authorization) {
        return getTab(BrowseManagerParams.getGamingQuery(), authorization);
    }

    public BrowseTab getNews(String authorization) {
        return getTab(BrowseManagerParams.getNewsQuery(), authorization);
    }

    public BrowseTab getMusic(String authorization) {
        return getTab(BrowseManagerParams.getMusicQuery(), authorization);
    }

    private BrowseResult getSection(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getAuthSection: authorization is null.");
            return null;
        }

        Call<BrowseResult> wrapper = mBrowseManagerSigned.getBrowseResult(query, authorization);

        BrowseResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getAuthSection: browse result is null");
        }

        return browseResult;
    }

    private BrowseResultContinuation getNextSection(String nextPageKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getNextAuthSection: authorization is null.");
            return null;
        }

        if (nextPageKey == null) {
            Log.e(TAG, "getNextAuthSection: next search key is null.");
            return null;
        }

        Call<BrowseResultContinuation> wrapper = mBrowseManagerSigned.continueBrowseResult(BrowseManagerParams.getNextBrowseQuery(nextPageKey), authorization);
        BrowseResultContinuation browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getNextAuthSection: browseResult is null. Maybe invalid next key: " + nextPageKey);
        }

        return browseResult;
    }

    public BrowseResultContinuation continueSection(String nextKey, String authorization) {
        return getNextSection(nextKey, authorization);
    }

    private TabbedBrowseResult getTabbedResult(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getTabbedResult: authorization is null.");
            return null;
        }

        Call<TabbedBrowseResult> wrapper = mBrowseManagerSigned.getTabbedBrowseResult(query, authorization);

        return RetrofitHelper.get(wrapper);
    }

    private RowsTabContinuation getNextTabbedResult(String nextKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getNextTabbedResult: authorization is null.");
            return null;
        }

        String query = BrowseManagerParams.getNextBrowseQuery(nextKey);

        Call<RowsTabContinuation> wrapper = mBrowseManagerSigned.continueTabbedBrowseResult(query, authorization);

        RowsTabContinuation browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private BrowseTab getTab(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getTab: authorization is null. Query: " + query);
            return null;
        }

        TabbedBrowseResult tabs = getTabbedResult(query, authorization);

        if (tabs == null) {
            Log.e(TAG, "getTab: tabs result is empty");
            return null;
        }

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

    public RowsTabContinuation continueTab(String nextKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getNextTab: authorization is null.");
            return null;
        }

        RowsTabContinuation nextHomeTabs = null;

        if (nextKey != null) {
            nextHomeTabs = getNextTabbedResult(nextKey, authorization);
        }

        if (nextHomeTabs == null) {
            Log.e(TAG, "getNextTab: tabs are empty");
            return null;
        }

        return nextHomeTabs;
    }
}
