package com.liskovsoft.youtubeapi.browse.ver2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.SectionTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.SectionTabResult;
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

    public SectionTab getHome() {
        return getRowsTab(BrowseManagerParams.getHomeQuery());
    }

    public SectionTab getGaming() {
        return getRowsTab(BrowseManagerParams.getGamingQuery());
    }

    public SectionTab getNews() {
        return getRowsTab(BrowseManagerParams.getNewsQuery());
    }

    public SectionTab getMusic() {
        return getRowsTab(BrowseManagerParams.getMusicQuery());
    }

    public GridTabContinuation continueGridTab(String nextPageKey) {
        return continueGridTabResult(nextPageKey, mVisitorData);
    }

    public SectionTabContinuation continueRowsTab(String nextPageKey) {
        SectionTabContinuation nextHomeTabs = null;

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

    private SectionTabResult getRowsTabResult(String query) {
        Call<SectionTabResult> wrapper = mBrowseManagerUnsigned.getSectionTabResult(query);

        return RetrofitHelper.get(wrapper);
    }

    private SectionTabContinuation continueRowsTabResult(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getContinuationQuery(nextKey);

        Call<SectionTabContinuation> wrapper = mBrowseManagerUnsigned.continueSectionTab(query, visitorData);

        return RetrofitHelper.get(wrapper);
    }

    private GridTabContinuation continueGridTabResult(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getContinuationQuery(nextKey);

        Call<GridTabContinuation> wrapper =
                mBrowseManagerUnsigned.continueGridTab(query, visitorData);

        return RetrofitHelper.get(wrapper);
    }

    private SectionTab getRowsTab(String query) {
        SectionTabResult tabs = getRowsTabResult(query);

        if (tabs == null) {
            Log.e(TAG, "getTabs: tabs result is empty");
            return null;
        }

        mVisitorData = tabs.getVisitorData();

        return firstNotEmpty(tabs);
    }

    private SectionTab firstNotEmpty(SectionTabResult tabs) {
        SectionTab result = null;

        if (tabs.getTabs() != null) {
            // find first not empty tab
            for (SectionTab browseTab : tabs.getTabs()) {
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
}
