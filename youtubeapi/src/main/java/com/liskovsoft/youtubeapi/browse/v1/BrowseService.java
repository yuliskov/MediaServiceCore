package com.liskovsoft.youtubeapi.browse.v1;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.auth.V1.AuthApi;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTabList;
import com.liskovsoft.youtubeapi.browse.v1.models.guide.Guide;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionTabList;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * For auth users only!<br/>
 * Wraps result from the {@link AuthApi} and {@link BrowseApi}
 */
public class BrowseService {
    private static final String TAG = BrowseService.class.getSimpleName();
    private final BrowseApi mBrowseManagerSigned;
    private final AppService mAppService;
    private static BrowseService sInstance;
    private Map<String, Guide> mGuideMap = new HashMap<>();

    private BrowseService() {
        mBrowseManagerSigned = RetrofitHelper.create(BrowseApi.class);
        mAppService = AppService.instance();
    }

    public static BrowseService instance() {
        if (sInstance == null) {
            sInstance = new BrowseService();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    public GridTab getSubscriptions() {
        return getGridTab(BrowseApiHelper.getSubscriptionsQuery());
    }

    public List<GridTab> getSubscribedChannelsByName() {
        List<GridTab> gridTabs = getSubscribedChannelsSection();

        return getPart(gridTabs, 1);
    }

    public List<GridTab> getSubscribedChannelsLastViewed() {
        List<GridTab> gridTabs = getSubscribedChannelsSection();

        if (gridTabs == null) {
            return null;
        }

        List<GridTab> result = getPart(gridTabs, 0);

        // all channels should be unique
        for (GridTab tab : getPart(gridTabs, 1)) {
            if (!result.contains(tab)) {
                result.add(tab);
            }
        }

        return result;
    }

    public List<GridTab> getSubscribedChannelsUpdate() {
        List<GridTab> subscribedChannelsByName = getSubscribedChannelsByName();

        if (subscribedChannelsByName == null) {
            return null;
        }

        Collections.sort(subscribedChannelsByName, (o1, o2) ->
                o1.hasNewContent() && !o2.hasNewContent() ? -1 : !o1.hasNewContent() && o2.hasNewContent() ? 1 : 0);

        return subscribedChannelsByName;
    }

    private List<GridTab> getSubscribedChannelsSection() {
        List<GridTab> gridTabs = getGridTabs(BrowseApiHelper.getSubscriptionsQuery());
        // Exclude All Subscriptions tab (first one)
        return gridTabs != null ? gridTabs.subList(1, gridTabs.size()) : null;
    }

    public List<GridTab> getSubscribedChannelsAll() {
        return getSubscribedChannelsSection();
    }

    public GridTab getHistory() {
        // Web client version (needs new parser, see history_25.01.2023.json)
        //return getGridTab(BrowseManagerParams.getHistoryQuery());

        return getGridTab(BrowseApiHelper.getMyLibraryQuery());
    }

    public List<GridTab> getPlaylists() {
        List<GridTab> playlists = getGridTabs(BrowseApiHelper.getMyLibraryQuery());

        if (playlists != null) {
            GridTab myVideos = playlists.get(1); // save "My videos" for later use
            //GridTab watchLater = playlists.get(2); // save "Watch later" for later use
            playlists.remove(3); // remove "Purchases"
            //playlists.remove(2); // remove "Watch later"
            playlists.remove(1); // remove "My videos"
            playlists.remove(0); // remove "History"
            playlists.add(myVideos); // add "My videos" to the end
            //playlists.add(watchLater); // add "Watch later" to the end
        }

        return playlists;
    }

    public SectionTab getHome() {
        return getSectionTab(BrowseApiHelper.getHomeQuery());
    }

    public SectionTab getGaming() {
        return getSectionTab(BrowseApiHelper.getGamingQuery());
    }

    public SectionTab getNews() {
        SectionTab newsTab = getSectionTab(BrowseApiHelper.getNewsQuery());

        //if (newsTab == null) {
        //    newsTab = getSectionTab(BrowseManagerParams.getNewsQueryUA());
        //}

        return newsTab;
    }

    public SectionTab getMusic() {
        return getSectionTab(BrowseApiHelper.getMusicQuery());
    }

    public SectionList getChannel(String channelId) {
        return getSectionList(BrowseApiHelper.getChannelQuery(channelId));
    }

    public SectionList getChannel(String channelId, String params) {
        return getSectionList(BrowseApiHelper.getChannelQuery(channelId, params));
    }

    /**
     * Special type of channel that could be found inside Music section (see Liked row More button)
     */
    public GridTab getGridChannel(String channelId) {
        return getGridTab(BrowseApiHelper.getChannelQuery(channelId));
    }

    /**
     * Make synchronized to fix race conditions between launcher channels and section items
     */
    synchronized private List<GridTab> getGridTabs(String query) {
        List<GridTab> result = null;

        Call<GridTabList> wrapper = mBrowseManagerSigned.getGridTabList(query);

        GridTabList browseResult = RetrofitHelper.get(wrapper);

        if (browseResult != null) {
            result = browseResult.getTabs();
        } else {
            Log.e(TAG, "getGridTabs: result is null");
        }

        return result;
    }

    private GridTab getGridTab(String query) {
        List<GridTab> gridTabs = getGridTabs(query);

        return firstWithItems(gridTabs);
    }

    private GridTab firstWithItems(List<GridTab> gridTabs) {
        if (gridTabs == null || gridTabs.isEmpty()) {
            return null;
        }

        for (GridTab tab : gridTabs) {
            if (tab != null && tab.getItemWrappers() != null) {
                return tab;
            }
        }

        return gridTabs.get(0); // fallback to first item (don't know whether it's used somewhere)
    }

    private List<GridTab> getGridTabs(int fromIndex, String query) {
        List<GridTab> gridTabs = getGridTabs(query);

        List<GridTab> result = null;

        if (gridTabs != null) {
            result = new ArrayList<>();

            for (int i = fromIndex; i < gridTabs.size(); i++) {
                GridTab tab = gridTabs.get(i);

                if (tab.isUnselectable()) {
                    continue;
                }

                result.add(tab);
            }
        }

        return result;
    }

    public SectionContinuation continueSection(String nextKey) {
        if (nextKey == null) {
            Log.e(TAG, "continueGridTabResult: next search key is null.");
            return null;
        }

        String query = BrowseApiHelper.getContinuationQuery(nextKey);
        Call<SectionContinuation> wrapper = mBrowseManagerSigned.continueSection(query, mAppService.getVisitorData());

        return RetrofitHelper.get(wrapper);
    }

    public GridTabContinuation continueGridTab(String nextKey) {
        if (nextKey == null) {
            Log.e(TAG, "continueGridTab: next search key is null.");
            return null;
        }

        String query = BrowseApiHelper.getContinuationQuery(nextKey);
        Call<GridTabContinuation> wrapper = mBrowseManagerSigned.continueGridTab(query);

        return RetrofitHelper.get(wrapper);
    }

    public SectionTabContinuation continueSectionTab(String nextKey) {
        if (nextKey == null) {
            Log.e(TAG, "continueGridTabResult: next search key is null.");
            return null;
        }

        String query = BrowseApiHelper.getContinuationQuery(nextKey);

        Call<SectionTabContinuation> wrapper = mBrowseManagerSigned.continueSectionTab(query, mAppService.getVisitorData());

        return RetrofitHelper.get(wrapper);
    }

    private Guide getGuide() {
        Call<Guide> wrapper = mBrowseManagerSigned.getGuide(BrowseApiHelper.getGuideQuery());

        return RetrofitHelper.get(wrapper);
    }

    public String getSuggestToken() {
        String result = null;

        String authorization = RetrofitOkHttpHelper.getAuthHeaders().get("Authorization");

        Guide guide = mGuideMap.get(authorization);

        if (guide == null) {
            mGuideMap.clear();
            guide = getGuide();

            if (guide != null) {
                mGuideMap.put(authorization, guide);
                result = guide.getSuggestToken();
            }
        } else {
            result = guide.getSuggestToken();
        }

        return result;
    }

    private SectionTabList getSectionTabList(String query) {
        Log.d(TAG, "Getting section tab list for query: %s", query);

        Call<SectionTabList> wrapper = mBrowseManagerSigned.getSectionTabList(query, mAppService.getVisitorData());

        return RetrofitHelper.get(wrapper);
    }

    private SectionTab getSectionTab(String query) {
        SectionTabList tabs = getSectionTabList(query);

        if (tabs == null) {
            Log.e(TAG, "getRowsTab: tabs result is empty");
            return null;
        }

        return firstNotEmpty(tabs);
    }

    private SectionList getSectionList(String query) {
        Call<SectionList> wrapper = mBrowseManagerSigned.getSectionList(query, mAppService.getVisitorData());

        return RetrofitHelper.get(wrapper);
    }

    private SectionTab firstNotEmpty(SectionTabList tabs) {
        SectionTab result = null;

        if (tabs.getTabs() != null) {
            // find first not empty tab
            for (SectionTab tab : tabs.getTabs()) {
                if (tab.getSections() != null) {
                    result = tab;
                    break;
                }
            }
        } else {
            Log.e(TAG, "firstNotEmpty: tabs are empty");
        }

        return result;
    }

    /**
     * Channels are split by different criteria e.g. (popular and alphanumeric order)
     */
    private List<GridTab> getPart(List<GridTab> gridTabs, int partIndex) {
        List<GridTab> azGridTabs = null;

        if (gridTabs != null) {
            azGridTabs = new ArrayList<>();

            int partIndexFound = 0;

            for (GridTab tab : gridTabs) {
                if (tab.isUnselectable()) {
                    partIndexFound++;
                } else if (partIndexFound == partIndex) {
                    azGridTabs.add(tab);
                }
            }

            if (azGridTabs.isEmpty()) {
                azGridTabs = gridTabs;
            }
        }

        return azGridTabs;
    }
}
