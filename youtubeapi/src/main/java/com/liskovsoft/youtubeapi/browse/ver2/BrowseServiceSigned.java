package com.liskovsoft.youtubeapi.browse.ver2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTabResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

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
        return getFirstGridTab(BrowseManagerParams.getSubscriptionsQuery(), authorization);
    }

    public GridTab getHistory(String authorization) {
        return getFirstGridTab(BrowseManagerParams.getMyLibraryQuery(), authorization);
    }

    public List<GridTab> getPlaylists(String authorization) {
        List<GridTab> libraryTabs = getGridTabs(BrowseManagerParams.getMyLibraryQuery(), authorization);

        List<GridTab> result = null;

        if (libraryTabs != null) {
            result = new ArrayList<>();

            boolean headerFound = false;

            for (GridTab tab : libraryTabs) {
                if (headerFound) {
                    result.add(tab);
                }

                if (tab.isUnselectable()) {
                    headerFound = true;
                }
            }
        }

        return result;
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

    private List<GridTab> getGridTabs(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getGridTabs: authorization is null.");
            return null;
        }

        List<GridTab> result = null;

        Call<GridTabResult> wrapper = mBrowseManagerSigned.getGridTabResult(query, authorization);

        GridTabResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult != null) {
            result = browseResult.getTabs();
        } else {
            Log.e(TAG, "getGridTabs: result is null");
        }

        return result;
    }

    private GridTab getFirstGridTab(String query, String authorization) {
        List<GridTab> gridTabs = getGridTabs(query, authorization);

        GridTab result = null;

        if (gridTabs != null) {
            result = gridTabs.get(0);
        }

        return result;
    }

    public GridTabContinuation continueGridTab(String nextKey, String authorization) {
        return continueGridTabResult(nextKey, authorization);
    }

    public RowsTabContinuation continueRowsTab(String nextKey, String authorization) {
        return continueRowsTabResult(nextKey, authorization);
    }

    private GridTabContinuation continueGridTabResult(String nextPageKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "continueGridTabResult: authorization is null.");
            return null;
        }

        if (nextPageKey == null) {
            Log.e(TAG, "continueGridTabResult: next search key is null.");
            return null;
        }

        String query = BrowseManagerParams.getContinuationQuery(nextPageKey);
        Call<GridTabContinuation> wrapper = mBrowseManagerSigned.continueGridTab(query, authorization);

        return RetrofitHelper.get(wrapper);
    }

    private RowsTabContinuation continueRowsTabResult(String nextPageKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "continueRowsTabResult: authorization is null.");
            return null;
        }

        if (nextPageKey == null) {
            Log.e(TAG, "continueGridTabResult: next search key is null.");
            return null;
        }

        String query = BrowseManagerParams.getContinuationQuery(nextPageKey);

        Call<RowsTabContinuation> wrapper = mBrowseManagerSigned.continueRowsTab(query, authorization);

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
