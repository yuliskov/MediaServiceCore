package com.liskovsoft.youtubeapi.browse.ver2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuationResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTabContinuationResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.RowsTabResult;
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

    public RowsTab getHome() {
        return getRowsTab(BrowseManagerParams.getHomeQuery());
    }

    public RowsTab getGaming() {
        return getRowsTab(BrowseManagerParams.getGamingQuery());
    }

    public RowsTab getNews() {
        return getRowsTab(BrowseManagerParams.getNewsQuery());
    }

    public RowsTab getMusic() {
        return getRowsTab(BrowseManagerParams.getMusicQuery());
    }

    public GridTabContinuationResult continueGridTab(String nextPageKey) {
        return continueGridTabResult(nextPageKey, mVisitorData);
    }

    public RowsTabContinuationResult continueRowsTab(String nextPageKey) {
        RowsTabContinuationResult nextHomeTabs = null;

        if (mVisitorData == null) {
            Log.e(TAG, "continueTab: visitor data is null");
        }

        if (nextPageKey != null) {
            nextHomeTabs = continueRowsTabResult(nextPageKey, mVisitorData);
        }

        if (nextHomeTabs == null) {
            Log.e(TAG, "NextHomeTabs are empty");
            return null;
        }

        return nextHomeTabs;
    }

    private RowsTabResult getRowsTabResult(String query) {
        Call<RowsTabResult> wrapper = mBrowseManagerUnsigned.getRowsTabResult(query);

        return RetrofitHelper.get(wrapper);
    }

    private RowsTabContinuationResult continueRowsTabResult(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getContinuationQuery(nextKey);

        Call<RowsTabContinuationResult> wrapper = mBrowseManagerUnsigned.continueRowsTabResult(query, visitorData);

        return RetrofitHelper.get(wrapper);
    }

    private GridTabContinuationResult continueGridTabResult(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getContinuationQuery(nextKey);

        Call<GridTabContinuationResult> wrapper =
                mBrowseManagerUnsigned.continueGridTabResult(query, visitorData);

        return RetrofitHelper.get(wrapper);
    }

    private RowsTab getRowsTab(String query) {
        RowsTabResult tabs = getRowsTabResult(query);

        if (tabs == null) {
            Log.e(TAG, "getTabs: tabs result is empty");
            return null;
        }

        mVisitorData = tabs.getVisitorData();

        return firstNotEmpty(tabs);
    }

    private RowsTab firstNotEmpty(RowsTabResult tabs) {
        RowsTab result = null;

        if (tabs.getTabs() != null) {
            // find first not empty tab
            for (RowsTab browseTab : tabs.getTabs()) {
                if (browseTab.getRows() != null) {
                    result = browseTab;
                    break;
                }
            }
        } else {
            Log.e(TAG, "firstNotEmpty: tabs are empty");
        }

        return result;
    }
}
