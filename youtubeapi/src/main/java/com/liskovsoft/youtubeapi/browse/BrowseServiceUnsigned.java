package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabList;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

public class BrowseServiceUnsigned {
    private static final String TAG = BrowseServiceUnsigned.class.getSimpleName();
    private static BrowseServiceUnsigned sInstance;
    private final BrowseManagerUnsigned mBrowseManagerUnsigned;
    private final AppService mAppService;

    private BrowseServiceUnsigned() {
        mBrowseManagerUnsigned = RetrofitHelper.withJsonPath(BrowseManagerUnsigned.class);
        mAppService = AppService.instance();
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
        return getSectionTab(BrowseManagerParams.getHomeQuery());
    }

    public SectionTab getGaming() {
        return getSectionTab(BrowseManagerParams.getGamingQuery());
    }

    public SectionTab getNews() {
        SectionTab newsTab = getSectionTab(BrowseManagerParams.getNewsQuery());

        //if (newsTab == null) {
        //    newsTab = getSectionTab(BrowseManagerParams.getNewsQueryUA());
        //}

        return newsTab;
    }

    public SectionTab getMusic() {
        return getSectionTab(BrowseManagerParams.getMusicQuery());
    }

    public SectionList getChannel(String channelId) {
        return getSectionList(BrowseManagerParams.getChannelQuery(channelId));
    }

    public SectionContinuation continueSection(String nextKey) {
        String query = BrowseManagerParams.getContinuationQuery(nextKey);

        Call<SectionContinuation> wrapper =
                mBrowseManagerUnsigned.continueSection(query, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    public GridTabContinuation continueGridTab(String nextKey) {
        String query = BrowseManagerParams.getContinuationQuery(nextKey);

        Call<GridTabContinuation> wrapper =
                mBrowseManagerUnsigned.continueGridTab(query, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private SectionTabList getSectionTabList(String query) {
        Call<SectionTabList> wrapper = mBrowseManagerUnsigned.getSectionTabList(query, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    public SectionTabContinuation continueSectionTab(String nextPageKey) {
        SectionTabContinuation nextHomeTabs = null;

        if (nextPageKey != null) {
            nextHomeTabs = continueSectionTab(nextPageKey, mAppService.getVisitorId());
        }

        if (nextHomeTabs == null) {
            Log.e(TAG, "NextHomeTabs are empty");
            return null;
        }

        return nextHomeTabs;
    }

    private SectionTabContinuation continueSectionTab(String nextKey, String visitorData) {
        String query = BrowseManagerParams.getContinuationQuery(nextKey);

        Call<SectionTabContinuation> wrapper = mBrowseManagerUnsigned.continueSectionTab(query, visitorData);

        return RetrofitHelper.get(wrapper);
    }

    private SectionTab getSectionTab(String query) {
        SectionTabList tabs = getSectionTabList(query);

        if (tabs == null) {
            Log.e(TAG, "getTabs: tabs result is empty");
            return null;
        }

        return firstNotEmpty(tabs);
    }

    private SectionList getSectionList(String query) {
        Call<SectionList> wrapper = mBrowseManagerUnsigned.getSectionList(query);

        return RetrofitHelper.get(wrapper);
    }

    private SectionTab firstNotEmpty(SectionTabList tabs) {
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
