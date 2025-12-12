package com.liskovsoft.youtubeapi.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liskovsoft.mediaserviceinterfaces.ContentService;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.actions.ActionsService;
import com.liskovsoft.youtubeapi.actions.ActionsServiceWrapper;
import com.liskovsoft.youtubeapi.browse.v2.BrowseService2;
import com.liskovsoft.youtubeapi.browse.v2.BrowseService2Wrapper;
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.SuggestionsGroup;
import com.liskovsoft.youtubeapi.next.v2.WatchNextService;
import com.liskovsoft.youtubeapi.next.v2.WatchNextServiceWrapper;
import com.liskovsoft.youtubeapi.rss.RssService;
import com.liskovsoft.youtubeapi.search.SearchServiceWrapper;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;
import com.liskovsoft.youtubeapi.utils.UtilsService;
import com.liskovsoft.youtubeapi.browse.v1.BrowseService;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.BaseMediaGroup;
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class YouTubeContentService implements ContentService {
    private static final String TAG = YouTubeContentService.class.getSimpleName();
    private static YouTubeContentService sInstance;

    private YouTubeContentService() {
        Log.d(TAG, "Starting...");
    }

    public static ContentService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeContentService();
        }

        return sInstance;
    }

    @Override
    public List<MediaGroup> getSearch(String searchText) {
        checkSigned();

        SearchResult search = getSearchService().getSearch(searchText);
        return YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public List<MediaGroup> getSearch(String searchText, int options) {
        checkSigned();

        SearchResult search = getSearchService().getSearch(searchText, options);
        return YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public Observable<List<MediaGroup>> getSearchObserve(String searchText) {
        return RxHelper.fromCallable(() -> getSearch(searchText));
    }

    @Override
    public Observable<List<MediaGroup>> getSearchObserve(String searchText, int options) {
        return RxHelper.fromCallable(() -> getSearch(searchText, options));
    }

    @Override
    public List<String> getSearchTags(String searchText) {
        checkSigned();

        return getSearchService().getSearchTags(searchText);
    }

    @Override
    public Observable<List<String>> getSearchTagsObserve(String searchText) {
        return RxHelper.fromCallable(() -> getSearchTags(searchText));
    }

    @Override
    public MediaGroup getSubscriptions() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        MediaGroup subscriptions = getBrowseService2().getSubscriptions();

        // TEMP fix. Subs not fully populated.
        if (subscriptions != null && subscriptions.getMediaItems() != null && subscriptions.getMediaItems().size() <= 5) {
            MediaGroup continuation = continueGroup(subscriptions);
            if (continuation == null || continuation.getMediaItems() == null || continuation.getMediaItems().isEmpty()) {
                if (getMediaServiceData() != null && !getMediaServiceData().isLegacyUIEnabled()) {
                    getMediaServiceData().setLegacyUIEnabled(true);
                    return getBrowseService2().getSubscriptions();
                }
            }
        }

        return subscriptions;
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsObserve() {
        return RxHelper.fromCallable(this::getSubscriptions);
    }

    @Override
    public MediaGroup getRssFeed(String... channelIds) {
        if (channelIds == null) {
            return null;
        }

        checkSigned();

        return RssService.getFeed(channelIds);
    }

    @Override
    public Observable<MediaGroup> getRssFeedObserve(String... channelIds) {
        return RxHelper.fromCallable(() -> getRssFeed(channelIds));
    }

    @Override
    public MediaGroup getSubscribedChannels() {
        checkSigned();

        return getBrowseService2().getSubscribedChannels();
    }

    @Override
    public MediaGroup getSubscribedChannelsByNewContent() {
        checkSigned();

        //List<GridTab> subscribedChannels = getBrowseService().getSubscribedChannelsUpdate();
        //return YouTubeMediaGroup.fromTabs(subscribedChannels, MediaGroup.TYPE_CHANNEL_UPLOADS);

        return getBrowseService2().getSubscribedChannelsByNewContent();
    }

    @Override
    public MediaGroup getSubscribedChannelsByName() {
        checkSigned();

        return getBrowseService2().getSubscribedChannelsByName();
    }

    @Override
    public MediaGroup getSubscribedChannelsByLastViewed() {
        checkSigned();

        return getBrowseService2().getSubscribedChannels();
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsObserve() {
        return RxHelper.fromCallable(this::getSubscribedChannels);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsByNewContentObserve() {
        return RxHelper.fromCallable(this::getSubscribedChannelsByNewContent);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsByNameObserve() {
        return RxHelper.fromCallable(this::getSubscribedChannelsByName);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsByLastViewedObserve() {
        return RxHelper.fromCallable(this::getSubscribedChannelsByLastViewed);
    }

    @Override
    public MediaGroup getRecommended() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        kotlin.Pair<List<MediaGroup>, String> home = getBrowseService2().getHome();

        List<MediaGroup> groups = home != null ? home.getFirst() : null;

        return groups != null && !groups.isEmpty() ? groups.get(0) : null;
    }

    @Override
    public Observable<MediaGroup> getRecommendedObserve() {
        return RxHelper.fromCallable(this::getRecommended);
    }

    @Override
    public MediaGroup getHistory() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        return getBrowseService2().getHistory();
    }

    @Override
    public Observable<MediaGroup> getHistoryObserve() {
        return RxHelper.fromCallable(this::getHistory);
    }

    @Override
    public MediaGroup getGroup(String reloadPageKey) {
        return getBrowseService2().getGroup(reloadPageKey, MediaGroup.TYPE_UNDEFINED, null);
    }

    @Override
    public MediaGroup getGroup(MediaItem mediaItem) {
        return mediaItem.getReloadPageKey() != null ?
                getBrowseService2().getGroup(mediaItem.getReloadPageKey(), mediaItem.getType(), mediaItem.getTitle()) :
                getBrowseService2().getChannelAsGrid(mediaItem.getChannelId());
    }

    @Override
    public Observable<MediaGroup> getGroupObserve(MediaItem mediaItem) {
        return RxHelper.fromCallable(() -> getGroup(mediaItem));
    }

    @Override
    public Observable<MediaGroup> getGroupObserve(String reloadPageKey) {
        return RxHelper.fromCallable(() -> getGroup(reloadPageKey));
    }

    @Override
    public List<MediaGroup> getHome() {
        checkSigned();

        List<MediaGroup> result = new ArrayList<>();
        kotlin.Pair<List<MediaGroup>, String> home = getBrowseService2().getHome();
        List<MediaGroup> groups = home != null ? home.getFirst() : null;

        if (groups == null) {
            Log.e(TAG, "Home group is empty");
            return null;
        }

        for (MediaGroup group : groups) {
            // Load chips
            if (group != null && group.isEmpty()) {
                List<MediaGroup> sections = getBrowseService2().continueEmptyGroup(group);

                if (sections != null) {
                    result.addAll(sections);
                }
            } else if (group != null) {
                result.add(group);
            }
        }

        return result;
    }

    @Override
    public Observable<List<MediaGroup>> getHomeObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, getBrowseService2().getHome());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getTrendingObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, getBrowseService2().getTrending());
        });
    }

    @Override
    public Observable<MediaGroup> getShortsObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            MediaGroup shorts = getBrowseService2().getShorts();

            if (shorts != null && shorts.getNextPageKey() != null) {
                emitGroup(emitter, shorts);
            } else {
                emitGroupPartial(emitter, shorts);
                emitGroup(emitter, getBrowseService2().getShorts2());
            }
        });
    }

    @Override
    public Observable<List<MediaGroup>> getKidsHomeObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, getBrowseService2().getKidsHome());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getSportsObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, getBrowseService2().getSports());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getLiveObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, getBrowseService2().getLive());
        });
    }

    @Override
    public Observable<MediaGroup> getMyVideosObserve() {
        return RxHelper.fromCallable(getBrowseService2()::getMyVideos);
    }

    @Override
    public Observable<List<MediaGroup>> getMusicObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            MediaGroup firstRow = getBrowseService2().getLikedMusic();
            emitGroupsPartial(emitter, Collections.singletonList(firstRow));

            emitGroups(emitter, getBrowseService2().getMusic());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getNewsObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, getBrowseService2().getNews());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getGamingObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, getBrowseService2().getGaming());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getChannelObserve(String channelId) {
        return getChannelObserve(channelId, null, null);
    }

    @Override
    public Observable<List<MediaGroup>> getChannelObserve(MediaItem item) {
        return getChannelObserve(item.getChannelId(), item.getTitle(), item.getParams());
    }

    private Observable<List<MediaGroup>> getChannelObserve(String channelId, String title, String params) {
        return RxHelper.create(emitter -> {
            checkSigned();

            String canonicalId = UtilsService.canonicalChannelId(channelId);

            // Special type of channel that could be found inside Music section (see Liked row More button)
            if (YouTubeHelper.isGridChannel(canonicalId)) {
                MediaGroup gridChannel = getBrowseService2().getGridChannel(canonicalId, params);

                if (gridChannel instanceof BaseMediaGroup && !gridChannel.isEmpty()) {
                    ((BaseMediaGroup) gridChannel).setTitle(title);
                    emitGroups(emitter, Collections.singletonList(gridChannel));
                } else {
                    kotlin.Pair<List<MediaGroup>, String> channel = getBrowseService2().getChannel(canonicalId, params);
                    emitGroups(emitter, channel);
                }
            } else {
                kotlin.Pair<List<MediaGroup>, String> channel = getBrowseService2().getChannel(canonicalId, params);
                emitGroups(emitter, channel);
            }
        });
    }

    @Nullable
    private List<MediaGroup> getChannelSorting(String channelId) {
        checkSigned();

        return getBrowseService2().getChannelSorting(channelId);
    }

    @Override
    public Observable<List<MediaGroup>> getChannelSortingObserve(String channelId) {
        return RxHelper.fromCallable(() -> getChannelSorting(channelId));
    }

    @Override
    public Observable<List<MediaGroup>> getChannelSortingObserve(MediaItem item) {
        return item != null && item.getChannelId() != null ? getChannelSortingObserve(item.getChannelId()) : null;
    }

    @Override
    public MediaGroup getChannelSearch(String channelId, String query) {
        checkSigned();

        return getBrowseService2().getChannelSearch(channelId, query);
    }

    @Override
    public Observable<MediaGroup> getChannelSearchObserve(String channelId, String query) {
        return RxHelper.fromCallable(() -> getChannelSearch(channelId, query));
    }

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, kotlin.Pair<List<MediaGroup>, String> groupsAndKey) {
        emitGroupsPartial(emitter, groupsAndKey);

        emitter.onComplete();
    }

    private void emitGroupsPartial(ObservableEmitter<List<MediaGroup>> emitter, kotlin.Pair<List<MediaGroup>, String> groupsAndKey) {
        if (groupsAndKey == null) {
            Log.e(TAG, "emitGroupsPartial: groupsAndKey is null");
            return;
        }

        List<MediaGroup> groups = groupsAndKey.getFirst();
        String nextKey = groupsAndKey.getSecond();

        while (groups != null && !groups.isEmpty()) {
            emitGroupsPartial(emitter, groups);
            groupsAndKey = getBrowseService2().continueSectionList(nextKey, groups.get(0).getType());
            groups = groupsAndKey != null ? groupsAndKey.getFirst() : null;
            nextKey = groupsAndKey != null ? groupsAndKey.getSecond() : null;
        }
    }

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, List<MediaGroup> groups) {
        emitGroupsPartial(emitter, groups);

        emitter.onComplete();
    }

    private void emitGroupsPartial(ObservableEmitter<List<MediaGroup>> emitter, List<MediaGroup> groups) {
        if (groups == null || groups.isEmpty()) {
            Log.e(TAG, "emitGroupsPartial: groups are null or empty");
            return;
        }

        MediaGroup firstGroup = groups.get(0);
        Log.d(TAG, "emitGroupsPartial: begin emitting group of type %s...", firstGroup != null ? firstGroup.getType() : null);

        List<MediaGroup> collector = new ArrayList<>();

        for (MediaGroup group : groups) { // Preserve positions
            if (group == null) {
                continue;
            }

            if (group.isEmpty()) { // Contains Chips (nested sections)?
                if (!collector.isEmpty()) {
                    emitter.onNext(collector);
                    collector = new ArrayList<>();
                }

                List<MediaGroup> sections = getBrowseService2().continueEmptyGroup(group);

                if (sections != null) {
                    emitter.onNext(sections);
                }
            } else {
                collector.add(group);
            }
        }

        if (!collector.isEmpty()) {
            emitter.onNext(collector);
        }
    }

    private void emitGroup(ObservableEmitter<MediaGroup> emitter, MediaGroup group) {
        emitGroupPartial(emitter, group);

        emitter.onComplete();
    }

    private void emitGroupPartial(ObservableEmitter<MediaGroup> emitter, MediaGroup group) {
        if (group == null) {
            Log.e(TAG, "emitGroupPartial: group is null");
            return;
        }

        Log.d(TAG, "emitGroupPartial: begin emitting group of type %s...", group.getType());

        emitter.onNext(group);
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        MediaGroup result = continueGroupChecked(mediaGroup);

        if (result == null) {
            return null;
        }

        if (result.isEmpty()) {
            // All contents has been filtered (e.g. shorts)
            return continueGroupChecked(result);
        }

        return result;
    }

    private MediaGroup continueGroupChecked(MediaGroup mediaGroup) {
        MediaGroup result = continueGroupInt(mediaGroup);

        if (result == null) {
            return null;
        }

        if (Helpers.equals(mediaGroup.getMediaItems(), result.getMediaItems()) &&
                Helpers.equals(mediaGroup.getNextPageKey(), result.getNextPageKey())) {
            // Result group is duplicate of the original. Seems that we've reached the end before. Skipping...
            return null;
        }

        return result;
    }

    private MediaGroup continueGroupInt(MediaGroup mediaGroup) {
        if (mediaGroup == null) {
            return null;
        }

        checkSigned();

        Log.d(TAG, "Continue group " + mediaGroup.getTitle() + "...");

        if (mediaGroup instanceof SuggestionsGroup) {
            return getWatchNextService().continueGroup(mediaGroup);
        }

        if (mediaGroup instanceof BaseMediaGroup) {
            MediaGroup group = null;

            // Fix channels with multiple empty groups (e.g. https://www.youtube.com/@RuhiCenetMedya/videos)
            for (int i = 0; i < 3; i++) {
                group = getBrowseService2().continueGroup(group == null ? mediaGroup : group);

                if (group == null || !group.isEmpty()) {
                    break;
                }
            }

            return group;
        }

        String nextKey = YouTubeHelper.extractNextKey(mediaGroup);

        switch (mediaGroup.getType()) {
            case MediaGroup.TYPE_SEARCH:
                return YouTubeMediaGroup.from(
                        getSearchService().continueSearch(nextKey),
                        mediaGroup);
            case MediaGroup.TYPE_HISTORY:
            case MediaGroup.TYPE_SUBSCRIPTIONS:
            case MediaGroup.TYPE_USER_PLAYLISTS:
            case MediaGroup.TYPE_CHANNEL_UPLOADS:
            case MediaGroup.TYPE_UNDEFINED:
                return YouTubeMediaGroup.from(
                        getBrowseService().continueGridTab(nextKey),
                        mediaGroup
                );
            default:
                return YouTubeMediaGroup.from(
                        getBrowseService().continueSection(nextKey),
                        mediaGroup
                );
        }
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup) {
        return RxHelper.fromCallable(() -> continueGroup(mediaGroup));
    }

    private void checkSigned() {
        getSignInService().checkAuth();
    }

    @Override
    public Observable<List<MediaGroup>> getPlaylistRowsObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            MediaGroup playlists = getPlaylists();

            if (playlists != null && playlists.getMediaItems() != null) {
                for (MediaItem playlist : playlists.getMediaItems()) {
                    kotlin.Pair<List<MediaGroup>, String> content = getBrowseService2().getChannel(playlist.getChannelId(), playlist.getParams());
                    if (content != null && content.getFirst() != null) {
                        MediaGroup mediaGroup = content.getFirst().get(0);
                        if (mediaGroup instanceof BaseMediaGroup) {
                            ((BaseMediaGroup) mediaGroup).setTitle(playlist.getTitle());
                        }
                        emitter.onNext(content.getFirst());
                    }
                }
                emitter.onComplete();
            } else {
                RxHelper.onError(emitter, "getPlaylistsRowObserve: the content is null");
            }
        });
    }

    @Override
    public Observable<MediaGroup> getPlaylistsObserve() {
        return RxHelper.fromCallable(this::getPlaylists);
    }

    private MediaGroup getPlaylists() {
        checkSigned();

        return getBrowseService2().getMyPlaylists();
    }

    @Override
    public void enableHistory(boolean enable) {
        if (enable) {
            getActionsService().resumeWatchHistory();
        } else {
            getActionsService().pauseWatchHistory();
        }
    }

    @Override
    public void clearHistory() {
        getActionsService().clearWatchHistory();
    }

    @Override
    public void clearSearchHistory() {
        getActionsService().clearSearchHistory();
        getSearchService().clearSearchHistory();
    }

    @NonNull
    private static YouTubeSignInService getSignInService() {
        return YouTubeSignInService.instance();
    }

    @NonNull
    private static ActionsService getActionsService() {
        return ActionsServiceWrapper.instance();
    }

    @NonNull
    private static SearchService getSearchService() {
        return SearchServiceWrapper.instance();
    }

    @NonNull
    private static BrowseService getBrowseService() {
        return BrowseService.instance();
    }

    @NonNull
    private static BrowseService2 getBrowseService2() {
        return BrowseService2Wrapper.INSTANCE;
    }

    @NonNull
    private static WatchNextService getWatchNextService() {
        return WatchNextServiceWrapper.INSTANCE;
    }

    @Nullable
    private static MediaServiceData getMediaServiceData() {
        return MediaServiceData.instance();
    }
}
