package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.Section;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;
import com.liskovsoft.youtubeapi.service.internal.MediaGroupManagerInt;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaGroupManagerSigned;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaGroupManagerUnsigned;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YouTubeMediaGroupManager implements MediaGroupManager {
    private static final String TAG = YouTubeMediaGroupManager.class.getSimpleName();
    private static YouTubeMediaGroupManager sInstance;
    private final YouTubeSignInManager mSignInManager;
    private MediaGroupManagerInt mMediaGroupManagerReal;

    private YouTubeMediaGroupManager() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInManager.instance();
    }

    public static MediaGroupManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupManager();
        }

        return sInstance;
    }

    @Override
    public MediaGroup getSearch(String searchText) {
        checkSigned();

        SearchResult search = mMediaGroupManagerReal.getSearch(searchText);
        return YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public MediaGroup getSearch(String searchText, int options) {
        checkSigned();

        SearchResult search = mMediaGroupManagerReal.getSearch(searchText, options);
        return YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaGroup> getSearchObserve(String searchText) {
        return ObservableHelper.fromNullable(() -> getSearch(searchText));
    }

    @Override
    public Observable<MediaGroup> getSearchObserve(String searchText, int options) {
        return ObservableHelper.fromNullable(() -> getSearch(searchText, options));
    }

    @Override
    public List<String> getSearchTags(String searchText) {
        checkSigned();

        return mMediaGroupManagerReal.getSearchTags(searchText);
    }

    @Override
    public Observable<List<String>> getSearchTagsObserve(String searchText) {
        return ObservableHelper.fromNullable(() -> getSearchTags(searchText));
    }

    @Override
    public MediaGroup getSubscriptions() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        GridTab subscriptions = mMediaGroupManagerReal.getSubscriptions();
        return YouTubeMediaGroup.from(subscriptions, MediaGroup.TYPE_SUBSCRIPTIONS);
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsObserve() {
        return ObservableHelper.fromNullable(this::getSubscriptions);
    }

    @Override
    public MediaGroup getSubscribedChannelsUpdate() {
        checkSigned();

        List<GridTab> subscribedChannels = mMediaGroupManagerReal.getSubscribedChannelsUpdate();

        return YouTubeMediaGroup.fromTabs(subscribedChannels, MediaGroup.TYPE_CHANNEL_UPLOADS);
    }

    @Override
    public MediaGroup getSubscribedChannelsAZ() {
        checkSigned();

        List<GridTab> subscribedChannels = mMediaGroupManagerReal.getSubscribedChannelsAZ();

        return YouTubeMediaGroup.fromTabs(subscribedChannels, MediaGroup.TYPE_CHANNEL_UPLOADS);
    }

    @Override
    public MediaGroup getSubscribedChannelsLastViewed() {
        checkSigned();

        List<GridTab> subscribedChannels = mMediaGroupManagerReal.getSubscribedChannelsLastViewed();

        return YouTubeMediaGroup.fromTabs(subscribedChannels, MediaGroup.TYPE_CHANNEL_UPLOADS);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsUpdateObserve() {
        return ObservableHelper.fromNullable(this::getSubscribedChannelsUpdate);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsAZObserve() {
        return ObservableHelper.fromNullable(this::getSubscribedChannelsAZ);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsLastViewedObserve() {
        return ObservableHelper.fromNullable(this::getSubscribedChannelsLastViewed);
    }

    @Override
    public MediaGroup getRecommended() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        SectionTab homeTab = mMediaGroupManagerReal.getHomeTab();

        Section recommended = null;

        List<Section> rows = homeTab.getSections();

        if (rows != null) {
            recommended = rows.get(0); // first one is recommended
        }

        MediaGroup result = YouTubeMediaGroup.from(recommended, MediaGroup.TYPE_RECOMMENDED);

        return result != null && result.isEmpty() ? continueGroup(result) : result; // Maybe Chip?
    }

    @Override
    public Observable<MediaGroup> getRecommendedObserve() {
        return ObservableHelper.fromNullable(this::getRecommended);
    }

    @Override
    public MediaGroup getHistory() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        GridTab history = mMediaGroupManagerReal.getHistory();
        return YouTubeMediaGroup.from(history, MediaGroup.TYPE_HISTORY);
    }

    @Override
    public Observable<MediaGroup> getHistoryObserve() {
        return ObservableHelper.fromNullable(this::getHistory);
    }

    @Override
    public MediaGroup getGroup(MediaItem mediaItem) {
        checkSigned();

        GridTabContinuation continuation = mMediaGroupManagerReal.continueGridTab(((YouTubeMediaItem) mediaItem).getReloadPageKey());

        return YouTubeMediaGroup.from(continuation);
    }

    @Override
    public Observable<MediaGroup> getGroupObserve(MediaItem mediaItem) {
        return ObservableHelper.fromNullable(() -> getGroup(mediaItem));
    }

    @Override
    public List<MediaGroup> getHome() {
        checkSigned();

        SectionTab tab = mMediaGroupManagerReal.getHomeTab();

        List<MediaGroup> result = new ArrayList<>();

        String nextPageKey = tab.getNextPageKey();
        List<MediaGroup> groups = YouTubeMediaGroup.from(tab.getSections(), MediaGroup.TYPE_HOME);

        if (groups.isEmpty()) {
            Log.e(TAG, "Home group is empty");
        }

        // Chips?
        for (MediaGroup group : groups) {
            if (group.isEmpty()) {
                continueGroup(group);
            }
        }

        while (!groups.isEmpty()) {
            result.addAll(groups);
            SectionTabContinuation continuation = mMediaGroupManagerReal.continueSectionTab(nextPageKey);

            if (continuation == null) {
                break;
            }

            nextPageKey = continuation.getNextPageKey();
            groups = YouTubeMediaGroup.from(continuation.getSections(), MediaGroup.TYPE_HOME);
        }

        return result;
    }

    @Override
    public Observable<List<MediaGroup>> getHomeObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getHomeTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_HOME);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getMusicObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getMusicTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_MUSIC);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getNewsObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getNewsTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_NEWS);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getGamingObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getGamingTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_GAMING);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getChannelObserve(String channelId) {
        return Observable.create(emitter -> {
            checkSigned();

            SectionList sectionList = mMediaGroupManagerReal.getChannel(channelId);

            emitGroups(emitter, sectionList, MediaGroup.TYPE_CHANNEL);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getChannelObserve(MediaItem item) {
        return getChannelObserve(item.getChannelId());
    }

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, SectionTab tab, int type) {
        if (tab == null) {
            String msg = String.format("emitGroups: BrowseTab of type %s is null", type);
            Log.e(TAG, msg);
            ObservableHelper.onError(emitter, msg);
            return;
        }

        Log.d(TAG, "emitGroups: begin emitting BrowseTab of type %s...", type);

        String nextPageKey = tab.getNextPageKey();
        List<MediaGroup> groups = YouTubeMediaGroup.from(tab.getSections(), type);

        if (groups.isEmpty()) {
            String msg = "Media group is empty: " + type;
            Log.e(TAG, msg);
            ObservableHelper.onError(emitter, msg);
        } else {
            while (!groups.isEmpty()) {
                for (MediaGroup group : groups) { // Preserve positions
                    if (group.isEmpty()) { // Contains Chips (nested sections)?
                        group = continueGroup(group);
                    }

                    if (group != null) {
                        emitter.onNext(new ArrayList<>(Collections.singletonList(group))); // convert immutable list to mutable
                    }
                }

                SectionTabContinuation continuation = mMediaGroupManagerReal.continueSectionTab(nextPageKey);

                if (continuation != null) {
                    nextPageKey = continuation.getNextPageKey();
                    groups = YouTubeMediaGroup.from(continuation.getSections(), type);
                } else {
                    break;
                }
            }

            emitter.onComplete();
        }
    }

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, SectionList sectionList, int type) {
        if (sectionList == null) {
            String msg = "emitGroups: SectionList is null";
            Log.e(TAG, msg);
            ObservableHelper.onError(emitter, msg);
            return;
        }

        List<MediaGroup> groups = YouTubeMediaGroup.from(sectionList.getSections(), type);

        if (groups.isEmpty()) {
            String msg = "emitGroups: SectionList content is null";
            Log.e(TAG, msg);
            ObservableHelper.onError(emitter, msg);
        } else {
            emitter.onNext(groups);
            emitter.onComplete();
        }
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        checkSigned();

        Log.d(TAG, "Continue group " + mediaGroup.getTitle() + "...");

        String nextKey = YouTubeMediaServiceHelper.extractNextKey(mediaGroup);

        switch (mediaGroup.getType()) {
            case MediaGroup.TYPE_SEARCH:
                return YouTubeMediaGroup.from(
                        mMediaGroupManagerReal.continueSearch(nextKey),
                        mediaGroup);
            case MediaGroup.TYPE_HISTORY:
            case MediaGroup.TYPE_SUBSCRIPTIONS:
            case MediaGroup.TYPE_USER_PLAYLISTS:
            case MediaGroup.TYPE_UNDEFINED:
                return YouTubeMediaGroup.from(
                        mMediaGroupManagerReal.continueGridTab(nextKey),
                        mediaGroup
                );
            default:
                return YouTubeMediaGroup.from(
                        mMediaGroupManagerReal.continueSection(nextKey),
                        mediaGroup
                );
        }
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup) {
        return ObservableHelper.fromNullable(() -> continueGroup(mediaGroup));
    }

    private void checkSigned() {
        if (mSignInManager.checkAuthHeader()) {
            Log.d(TAG, "User signed.");

            mMediaGroupManagerReal = YouTubeMediaGroupManagerSigned.instance();
            YouTubeMediaGroupManagerUnsigned.unhold();
        } else {
            Log.d(TAG, "User doesn't signed.");

            mMediaGroupManagerReal = YouTubeMediaGroupManagerUnsigned.instance();
            YouTubeMediaGroupManagerSigned.unhold();
        }
    }

    @Override
    public Observable<List<MediaGroup>> getPlaylistsObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            List<GridTab> tabs = mMediaGroupManagerReal.getPlaylists();

            if (tabs != null && tabs.size() > 0) {
                for (GridTab tab : tabs) {
                    GridTabContinuation tabContinuation = mMediaGroupManagerReal.continueGridTab(tab.getReloadPageKey());

                    if (tabContinuation != null) {
                        ArrayList<MediaGroup> list = new ArrayList<>();
                        YouTubeMediaGroup mediaGroup = new YouTubeMediaGroup(MediaGroup.TYPE_USER_PLAYLISTS);
                        mediaGroup.setTitle(tab.getTitle()); // id calculated by title hashcode
                        list.add(YouTubeMediaGroup.from(tabContinuation, mediaGroup));
                        emitter.onNext(list);
                    }
                }

                emitter.onComplete();
            } else {
                ObservableHelper.onError(emitter, "getPlaylistsObserve: tab is null");
            }
        });
    }

    @Override
    public Observable<MediaGroup> getEmptyPlaylistsObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            List<GridTab> tabs = mMediaGroupManagerReal.getPlaylists();

            if (tabs != null && tabs.size() > 0) {
                emitter.onNext(YouTubeMediaGroup.fromTabs(tabs, MediaGroup.TYPE_USER_PLAYLISTS));
                emitter.onComplete();
            } else {
                ObservableHelper.onError(emitter, "getEmptyPlaylistsObserve: tab is null");
            }
        });
    }
}
