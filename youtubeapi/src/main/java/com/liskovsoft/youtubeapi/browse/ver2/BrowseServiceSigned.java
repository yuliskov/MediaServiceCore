package com.liskovsoft.youtubeapi.browse.ver2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuationResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTabContinuationResult;
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

        return browseResult.getTabs().get(0); // TODO: find first non-empty
    }

    public GridTabContinuationResult continueGridTab(String nextKey, String authorization) {
        return continueGridTabResult(nextKey, authorization);
    }

    public RowsTabContinuationResult continueRowsTab(String nextKey, String authorization) {
        return continueRowsTabResult(nextKey, authorization);
    }

    private GridTabContinuationResult continueGridTabResult(String nextPageKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "continueGridTabResult: authorization is null.");
            return null;
        }

        if (nextPageKey == null) {
            Log.e(TAG, "continueGridTabResult: next search key is null.");
            return null;
        }

        String query = BrowseManagerParams.getContinuationQuery(nextPageKey);
        Call<GridTabContinuationResult> wrapper = mBrowseManagerSigned.continueGridTabResult(query, authorization);

        return RetrofitHelper.get(wrapper);
    }

    private RowsTabContinuationResult continueRowsTabResult(String nextPageKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "continueRowsTabResult: authorization is null.");
            return null;
        }

        if (nextPageKey == null) {
            Log.e(TAG, "continueGridTabResult: next search key is null.");
            return null;
        }

        String query = BrowseManagerParams.getContinuationQuery(nextPageKey);

        Call<RowsTabContinuationResult> wrapper = mBrowseManagerSigned.continueRowsTabResult(query, authorization);

        return RetrofitHelper.get(wrapper);
    }

    private RowsTabResult getRowsTabResult(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getRowsTabResult: authorization is null.");
            return null;
        }

        Call<RowsTabResult> wrapper = mBrowseManagerSigned.getRowsTabResult(query, authorization);

        return RetrofitHelper.get(wrapper);
    }

    private RowsTab getRowsTab(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getRowsTab: authorization is null. Query: " + query);
            return null;
        }

        RowsTabResult tabs = getRowsTabResult(query, authorization);

        if (tabs == null) {
            Log.e(TAG, "getRowsTab: tabs result is empty");
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
}
