package com.liskovsoft.youtubeapi.browse.ver2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTabResult;
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

    public GridTab getSubscriptions(String authorization) {
        return getGridTab(BrowseManagerParams.getSubscriptionsQuery(), authorization);
    }

    public GridTab getHistory(String authorization) {
        return getGridTab(BrowseManagerParams.getHistoryQuery(), authorization);
    }

    public RowsTab getHome(String authorization) {
        return getRowsTab(BrowseManagerParams.getHomeQuery(), authorization);
    }

    public RowsTab getGaming(String authorization) {
        return getRowsTab(BrowseManagerParams.getGamingQuery(), authorization);
    }

    public RowsTab getNews(String authorization) {
        return getRowsTab(BrowseManagerParams.getNewsQuery(), authorization);
    }

    public RowsTab getMusic(String authorization) {
        return getRowsTab(BrowseManagerParams.getMusicQuery(), authorization);
    }

    private GridTab getGridTab(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getAuthSection: authorization is null.");
            return null;
        }

        Call<GridTabResult> wrapper = mBrowseManagerSigned.getGridTabResult(query, authorization);

        GridTabResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getAuthSection: browse result is null");
        }

        return browseResult.getTabs().get(0); // TODO: first first non-empty
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

        Call<BrowseResultContinuation> wrapper = mBrowseManagerSigned.continueGridTabResult(BrowseManagerParams.getContinuationQuery(nextPageKey), authorization);
        BrowseResultContinuation browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getNextAuthSection: browseResult is null. Maybe invalid next key: " + nextPageKey);
        }

        return browseResult;
    }

    public BrowseResultContinuation continueSection(String nextKey, String authorization) {
        return getNextSection(nextKey, authorization);
    }

    private TabbedBrowseResultContinuation getNextTabbedResult(String nextKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getNextTabbedResult: authorization is null.");
            return null;
        }

        String query = BrowseManagerParams.getContinuationQuery(nextKey);

        Call<TabbedBrowseResultContinuation> wrapper = mBrowseManagerSigned.continueRowsTabResult(query, authorization);

        TabbedBrowseResultContinuation browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private RowsTabResult getRowsTabResult(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getTabbedResult: authorization is null.");
            return null;
        }

        Call<RowsTabResult> wrapper = mBrowseManagerSigned.getRowsTabResult(query, authorization);

        return RetrofitHelper.get(wrapper);
    }

    private RowsTab getRowsTab(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getTab: authorization is null. Query: " + query);
            return null;
        }

        RowsTabResult tabs = getRowsTabResult(query, authorization);

        if (tabs == null) {
            Log.e(TAG, "getTab: tabs result is empty");
            return null;
        }

        return firstNotEmpty(tabs);
    }

    private RowsTab firstNotEmpty(RowsTabResult tabs) {
        RowsTab result = null;

        if (tabs.getTabs() != null) {
            // find first not empty tab
            for (RowsTab tab : tabs.getTabs()) {
                if (tab.getRows() != null) {
                    result = tab;
                    break;
                }
            }
        } else {
            Log.e(TAG, "firstNotEmpty: tabs are empty");
        }

        return result;
    }

    public TabbedBrowseResultContinuation continueTab(String nextKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getNextTab: authorization is null.");
            return null;
        }

        TabbedBrowseResultContinuation nextHomeTabs = null;

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
