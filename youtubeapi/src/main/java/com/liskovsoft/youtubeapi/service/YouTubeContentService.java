package com.liskovsoft.youtubeapi.service;

import androidx.annotation.Nullable;

import com.liskovsoft.mediaserviceinterfaces.ContentService;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.actions.ActionsService;
import com.liskovsoft.youtubeapi.actions.ActionsServiceWrapper;
import com.liskovsoft.youtubeapi.browse.v2.BrowseService2Wrapper;
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.SuggestionsGroup;
import com.liskovsoft.youtubeapi.next.v2.WatchNextServiceWrapper;
import com.liskovsoft.youtubeapi.rss.RssService;
import com.liskovsoft.youtubeapi.search.SearchServiceWrapper;
import com.liskovsoft.youtubeapi.utils.UtilsService;
import com.liskovsoft.youtubeapi.browse.v1.BrowseService;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.browse.v2.BrowseService2;
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.BaseMediaGroup;
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YouTubeContentService implements ContentService {
    private static final String TAG = YouTubeContentService.class.getSimpleName();
    private static YouTubeContentService sInstance;
    private final YouTubeSignInService mSignInService;
    private final ActionsService mActionsService;
    private final SearchService mSearchService;
    private final BrowseService mBrowseService;
    private final BrowseService2 mBrowseService2;
    private final WatchNextServiceWrapper mWatchNextService;

    private YouTubeContentService() {
        Log.d(TAG, "Starting...");

        mSignInService = YouTubeSignInService.instance();
        mActionsService = ActionsServiceWrapper.instance();
        mSearchService = SearchServiceWrapper.instance();
        mBrowseService = BrowseService.instance();
        mBrowseService2 = BrowseService2Wrapper.getInstance();
        mWatchNextService = WatchNextServiceWrapper.getInstance();
    }

    public static ContentService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeContentService();
        }

        return sInstance;
    }

    @Override
    public MediaGroup getSearch(String searchText) {
        checkSigned();

        SearchResult search = mSearchService.getSearch(searchText);
        List<MediaGroup> groups = YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
        return groups != null && groups.size() > 0 ? groups.get(0) : null;
    }

    @Override
    public MediaGroup getSearch(String searchText, int options) {
        checkSigned();

        SearchResult search = mSearchService.getSearch(searchText, options);
        List<MediaGroup> groups = YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
        return groups != null && groups.size() > 0 ? groups.get(0) : null;
    }

    @Override
    public List<MediaGroup> getSearchAlt(String searchText) {
        checkSigned();

        SearchResult search = mSearchService.getSearch(searchText);
        return YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public List<MediaGroup> getSearchAlt(String searchText, int options) {
        checkSigned();

        SearchResult search = mSearchService.getSearch(searchText, options);
        return YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaGroup> getSearchObserve(String searchText) {
        return RxHelper.fromNullable(() -> getSearch(searchText));
    }

    @Override
    public Observable<MediaGroup> getSearchObserve(String searchText, int options) {
        return RxHelper.fromNullable(() -> getSearch(searchText, options));
    }

    @Override
    public Observable<List<MediaGroup>> getSearchAltObserve(String searchText) {
        return RxHelper.fromNullable(() -> getSearchAlt(searchText));
    }

    @Override
    public Observable<List<MediaGroup>> getSearchAltObserve(String searchText, int options) {
        return RxHelper.fromNullable(() -> getSearchAlt(searchText, options));
    }

    @Override
    public List<String> getSearchTags(String searchText) {
        checkSigned();

        return mSearchService.getSearchTags(searchText);
    }

    @Override
    public Observable<List<String>> getSearchTagsObserve(String searchText) {
        return RxHelper.fromNullable(() -> getSearchTags(searchText));
    }

    @Override
    public MediaGroup getSubscriptions() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        return mBrowseService2.getSubscriptions();
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsObserve() {
        return RxHelper.fromNullable(this::getSubscriptions);
    }

    @Override
    public MediaGroup getSubscriptions(String... channelIds) {
        checkSigned();

        return RssService.getFeed(channelIds);
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsObserve(String... channelIds) {
        return RxHelper.fromNullable(() -> getSubscriptions(channelIds));
    }

    @Override
    public MediaGroup getSubscribedChannels() {
        checkSigned();

        return mBrowseService2.getSubscribedChannels();
    }

    @Override
    public MediaGroup getSubscribedChannelsByNewContent() {
        checkSigned();

        //List<GridTab> subscribedChannels = mBrowseService.getSubscribedChannelsUpdate();
        //return YouTubeMediaGroup.fromTabs(subscribedChannels, MediaGroup.TYPE_CHANNEL_UPLOADS);

        return mBrowseService2.getSubscribedChannelsByNewContent();
    }

    @Override
    public MediaGroup getSubscribedChannelsByName() {
        checkSigned();

        return mBrowseService2.getSubscribedChannelsByName();
    }

    @Override
    public MediaGroup getSubscribedChannelsByLastViewed() {
        checkSigned();

        return mBrowseService2.getSubscribedChannels();
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsObserve() {
        return RxHelper.fromNullable(this::getSubscribedChannels);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsByNewContentObserve() {
        return RxHelper.fromNullable(this::getSubscribedChannelsByNewContent);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsByNameObserve() {
        return RxHelper.fromNullable(this::getSubscribedChannelsByName);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsByLastViewedObserve() {
        return RxHelper.fromNullable(this::getSubscribedChannelsByLastViewed);
    }

    //@Override
    //public MediaGroup getRecommended() {
    //    Log.d(TAG, "Getting recommended...");
    //
    //    checkSigned();
    //
    //    SectionTab homeTab = mBrowseService.getHome();
    //
    //    List<MediaGroup> groups = YouTubeMediaGroup.from(homeTab.getSections(), MediaGroup.TYPE_RECOMMENDED);
    //
    //    MediaGroup result = null;
    //
    //    if (!groups.isEmpty()) {
    //        result = groups.get(0); // first one is recommended
    //    }
    //
    //    return result != null && result.isEmpty() ? continueGroup(result) : result; // Maybe a Chip?
    //}

    @Override
    public MediaGroup getRecommended() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        kotlin.Pair<List<MediaGroup>, String> home = mBrowseService2.getHome();

        List<MediaGroup> groups = home != null ? home.getFirst() : null;

        return groups != null && !groups.isEmpty() ? groups.get(0) : null;
    }

    @Override
    public Observable<MediaGroup> getRecommendedObserve() {
        return RxHelper.fromNullable(this::getRecommended);
    }

    @Override
    public MediaGroup getHistory() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        return mBrowseService2.getHistory();
    }

    @Override
    public Observable<MediaGroup> getHistoryObserve() {
        return RxHelper.fromNullable(this::getHistory);
    }

    @Override
    public MediaGroup getGroup(String reloadPageKey) {
        return mBrowseService2.getGroup(reloadPageKey, MediaGroup.TYPE_UNDEFINED, null);
    }

    @Override
    public MediaGroup getGroup(MediaItem mediaItem) {
        return mediaItem.getReloadPageKey() != null ?
                mBrowseService2.getGroup(mediaItem.getReloadPageKey(), mediaItem.getType(), mediaItem.getTitle()) :
                mBrowseService2.getChannelAsGrid(mediaItem.getChannelId());
    }

    @Override
    public Observable<MediaGroup> getGroupObserve(MediaItem mediaItem) {
        return RxHelper.fromNullable(() -> getGroup(mediaItem));
    }

    @Override
    public Observable<MediaGroup> getGroupObserve(String reloadPageKey) {
        return RxHelper.fromNullable(() -> getGroup(reloadPageKey));
    }

    @Override
    public List<MediaGroup> getHome() {
        checkSigned();

        List<MediaGroup> result = new ArrayList<>();
        kotlin.Pair<List<MediaGroup>, String> home = mBrowseService2.getHome();
        List<MediaGroup> groups = home != null ? home.getFirst() : null;

        if (groups == null) {
            Log.e(TAG, "Home group is empty");
            return null;
        }

        for (MediaGroup group : groups) {
            // Load chips
            if (group != null && group.isEmpty()) {
                List<MediaGroup> sections = mBrowseService2.continueEmptyGroup(group);

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

            emitGroups(emitter, mBrowseService2.getHome());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getTrendingObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, mBrowseService2.getTrending());
        });
    }

    private MediaGroup getShorts() {
        checkSigned();

        return mBrowseService2.getShorts();
    }

    @Override
    public Observable<MediaGroup> getShortsObserve() {
        return RxHelper.fromNullable(this::getShorts);
    }

    @Override
    public Observable<List<MediaGroup>> getKidsHomeObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, mBrowseService2.getKidsHome());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getSportsObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, mBrowseService2.getSports());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getLiveObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, mBrowseService2.getLive());
        });
    }

    @Override
    public Observable<MediaGroup> getMyVideosObserve() {
        return RxHelper.fromNullable(mBrowseService2::getMyVideos);
    }

    @Override
    public Observable<List<MediaGroup>> getMusicObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            MediaGroup firstRow = mBrowseService2.getLikedMusic();
            emitGroupsPartial(emitter, Collections.singletonList(firstRow));

            emitGroups(emitter, mBrowseService2.getMusic());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getNewsObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, mBrowseService2.getNews());
        });
    }

    @Override
    public Observable<List<MediaGroup>> getGamingObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            emitGroups(emitter, mBrowseService2.getGaming());
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
                MediaGroup gridChannel = mBrowseService2.getGridChannel(canonicalId);

                if (gridChannel instanceof BaseMediaGroup && !gridChannel.isEmpty()) {
                    ((BaseMediaGroup) gridChannel).setTitle(title);
                    emitGroups(emitter, Collections.singletonList(gridChannel));
                } else {
                    kotlin.Pair<List<MediaGroup>, String> channel = mBrowseService2.getChannel(canonicalId, params);
                    emitGroups(emitter, channel);
                }
            } else {
                kotlin.Pair<List<MediaGroup>, String> channel = mBrowseService2.getChannel(canonicalId, params);
                emitGroups(emitter, channel);
            }
        });
    }

    @Nullable
    private List<MediaGroup> getChannelSorting(String channelId) {
        checkSigned();

        return mBrowseService2.getChannelSorting(channelId);
    }

    @Override
    public Observable<List<MediaGroup>> getChannelSortingObserve(String channelId) {
        return RxHelper.fromNullable(() -> getChannelSorting(channelId));
    }

    @Override
    public Observable<List<MediaGroup>> getChannelSortingObserve(MediaItem item) {
        return item != null && item.getChannelId() != null ? getChannelSortingObserve(item.getChannelId()) : null;
    }

    @Override
    public MediaGroup getChannelSearch(String channelId, String query) {
        checkSigned();

        return mBrowseService2.getChannelSearch(channelId, query);
    }

    @Override
    public Observable<MediaGroup> getChannelSearchObserve(String channelId, String query) {
        return RxHelper.fromNullable(() -> getChannelSearch(channelId, query));
    }

    //private Observable<List<MediaGroup>> emitHome() {
    //    return RxHelper.create(emitter -> {
    //        checkSigned();
    //
    //        List<MediaGroup> sections = mBrowseService2.getHome();
    //
    //        if (sections != null && sections.size() > 5) {
    //            emitGroups(emitter, sections);
    //        } else {
    //            // Fallback to old algo if user chrome page has no chips (why?)
    //            SectionTab tab = mBrowseService.getHome();
    //
    //            if (tab != null && tab.getSections() != null && !tab.getSections().isEmpty()) {
    //                tab.getSections().remove(0); // replace Recommended
    //            }
    //
    //            List<MediaGroup> subGroup = sections != null && sections.size() > 2 ? sections.subList(0, 2) : sections;
    //
    //            emitGroupsPartial(emitter, subGroup); // get Recommended only
    //            emitGroups(emitter, tab, MediaGroup.TYPE_HOME);
    //        }
    //    });
    //}

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, kotlin.Pair<List<MediaGroup>, String> result) {
        if (result == null) {
            String msg = "emitGroups2: groups are null or empty";
            Log.e(TAG, msg);
            RxHelper.onError(emitter, msg);
            return;
        }

        List<MediaGroup> groups = result.getFirst();
        String nextKey = result.getSecond();

        while (groups != null && !groups.isEmpty()) {
            emitGroupsPartial(emitter, groups);
            result = mBrowseService2.continueSectionList(nextKey, groups.get(0).getType());
            groups = result != null ? result.getFirst() : null;
            nextKey = result != null ? result.getSecond() : null;
        }

        emitter.onComplete();
    }

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, List<MediaGroup> groups) {
        if (groups == null || groups.isEmpty()) {
            String msg = "emitGroups2: groups are null or empty";
            Log.e(TAG, msg);
            RxHelper.onError(emitter, msg);
            return;
        }

        emitGroupsPartial(emitter, groups);

        emitter.onComplete();
    }

    private void emitGroupsPartial(ObservableEmitter<List<MediaGroup>> emitter, List<MediaGroup> groups) {
        if (groups == null || groups.isEmpty()) {
            return;
        }

        MediaGroup firstGroup = groups.get(0);
        Log.d(TAG, "emitGroups: begin emitting group of type %s...", firstGroup != null ? firstGroup.getType() : null);

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

                List<MediaGroup> sections = mBrowseService2.continueEmptyGroup(group);

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

    //private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, SectionTab tab, int type) {
    //    if (tab == null) {
    //        String msg = String.format("emitGroups: BrowseTab of type %s is null", type);
    //        Log.e(TAG, msg);
    //        RxHelper.onError(emitter, msg);
    //        return;
    //    }
    //
    //    Log.d(TAG, "emitGroups: begin emitting BrowseTab of type %s...", type);
    //
    //    String nextPageKey = tab.getNextPageKey();
    //    List<MediaGroup> groups = YouTubeMediaGroup.from(tab.getSections(), type);
    //
    //    if (groups.isEmpty()) {
    //        String msg = "Media group is empty: " + type;
    //        Log.e(TAG, msg);
    //        RxHelper.onError(emitter, msg);
    //    } else {
    //        while (!groups.isEmpty()) {
    //            for (MediaGroup group : groups) { // Preserve positions
    //                if (group.isEmpty()) { // Contains Chips (nested sections)?
    //                    group = continueGroup(group);
    //                }
    //
    //                if (group != null) {
    //                    emitter.onNext(new ArrayList<>(Collections.singletonList(group))); // convert immutable list to mutable
    //                }
    //            }
    //
    //            SectionTabContinuation continuation = mBrowseService.continueSectionTab(nextPageKey);
    //
    //            if (continuation != null) {
    //                nextPageKey = continuation.getNextPageKey();
    //                groups = YouTubeMediaGroup.from(continuation.getSections(), type);
    //            } else {
    //                break;
    //            }
    //        }
    //
    //        emitter.onComplete();
    //    }
    //}

    //private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, SectionList sectionList, int type) {
    //    if (sectionList == null) {
    //        String msg = "emitGroups: SectionList is null";
    //        Log.e(TAG, msg);
    //        RxHelper.onError(emitter, msg);
    //        return;
    //    }
    //
    //    List<MediaGroup> groups = YouTubeMediaGroup.from(sectionList.getSections(), type);
    //
    //    if (groups.isEmpty()) {
    //        String msg = "emitGroups: SectionList content is null";
    //        Log.e(TAG, msg);
    //        RxHelper.onError(emitter, msg);
    //    } else {
    //        emitter.onNext(groups);
    //        emitter.onComplete();
    //    }
    //}

    //private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, GridTab grid, int type) {
    //    if (grid == null) {
    //        String msg = "emitGroups: Grid is null";
    //        Log.e(TAG, msg);
    //        RxHelper.onError(emitter, msg);
    //        return;
    //    }
    //
    //    MediaGroup group = YouTubeMediaGroup.from(grid, type);
    //
    //    if (group == null) {
    //        String msg = "emitGroups: Grid content is null";
    //        Log.e(TAG, msg);
    //        RxHelper.onError(emitter, msg);
    //    } else {
    //        emitter.onNext(Collections.singletonList(group));
    //        emitter.onComplete();
    //    }
    //}

    //@Override
    //public MediaGroup continueGroup(MediaGroup mediaGroup) {
    //    checkSigned();
    //
    //    Log.d(TAG, "Continue group " + mediaGroup.getTitle() + "...");
    //
    //    String nextKey = YouTubeHelper.extractNextKey(mediaGroup);
    //
    //    switch (mediaGroup.getType()) {
    //        case MediaGroup.TYPE_SEARCH:
    //            return YouTubeMediaGroup.from(
    //                    mSearchService.continueSearch(nextKey),
    //                    mediaGroup);
    //        case MediaGroup.TYPE_HISTORY:
    //        case MediaGroup.TYPE_SUBSCRIPTIONS:
    //        case MediaGroup.TYPE_USER_PLAYLISTS:
    //        case MediaGroup.TYPE_CHANNEL_UPLOADS:
    //        case MediaGroup.TYPE_UNDEFINED:
    //            return YouTubeMediaGroup.from(
    //                    mBrowseService.continueGridTab(nextKey),
    //                    mediaGroup
    //            );
    //        default:
    //            return YouTubeMediaGroup.from(
    //                    mBrowseService.continueSection(nextKey),
    //                    mediaGroup
    //            );
    //    }
    //}

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        MediaGroup result = continueGroupReal(mediaGroup);

        if (result != null &&
                Helpers.equals(mediaGroup.getMediaItems(), result.getMediaItems()) &&
                Helpers.equals(mediaGroup.getNextPageKey(), result.getNextPageKey())) {
            // Result group is duplicate of the original. Seems that we've reached the end before. Skipping...
            return null;
        }

        return result;
    }

    private MediaGroup continueGroupReal(MediaGroup mediaGroup) {
        if (mediaGroup == null) {
            return null;
        }

        checkSigned();

        Log.d(TAG, "Continue group " + mediaGroup.getTitle() + "...");

        if (mediaGroup instanceof SuggestionsGroup) {
            return mWatchNextService.continueGroup(mediaGroup);
        }

        if (mediaGroup instanceof BaseMediaGroup) {
            MediaGroup group = null;

            // Fix channels with multiple empty groups (e.g. https://www.youtube.com/@RuhiCenetMedya/videos)
            for (int i = 0; i < 3; i++) {
                group = mBrowseService2.continueGroup(group == null ? mediaGroup : group);

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
                        mSearchService.continueSearch(nextKey),
                        mediaGroup);
            case MediaGroup.TYPE_HISTORY:
            case MediaGroup.TYPE_SUBSCRIPTIONS:
            case MediaGroup.TYPE_USER_PLAYLISTS:
            case MediaGroup.TYPE_CHANNEL_UPLOADS:
            case MediaGroup.TYPE_UNDEFINED:
                return YouTubeMediaGroup.from(
                        mBrowseService.continueGridTab(nextKey),
                        mediaGroup
                );
            default:
                return YouTubeMediaGroup.from(
                        mBrowseService.continueSection(nextKey),
                        mediaGroup
                );
        }
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup) {
        return RxHelper.fromNullable(() -> continueGroup(mediaGroup));
    }

    private void checkSigned() {
        mSignInService.checkAuth();
    }

    @Override
    public Observable<List<MediaGroup>> getPlaylistRowsObserve() {
        return RxHelper.create(emitter -> {
            checkSigned();

            MediaGroup playlists = getPlaylists();

            if (playlists != null && playlists.getMediaItems() != null) {
                for (MediaItem playlist : playlists.getMediaItems()) {
                    kotlin.Pair<List<MediaGroup>, String> content = mBrowseService2.getChannel(playlist.getChannelId(), playlist.getParams());
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

    //@Override
    //public Observable<List<MediaGroup>> getPlaylistRowsObserve() {
    //    return RxHelper.create(emitter -> {
    //        checkSigned();
    //
    //        List<GridTab> tabs = mBrowseService.getPlaylists();
    //
    //        if (tabs != null && tabs.size() > 0) {
    //            for (GridTab tab : tabs) {
    //                GridTabContinuation tabContinuation = mBrowseService.continueGridTab(tab.getReloadPageKey());
    //
    //                if (tabContinuation != null) {
    //                    ArrayList<MediaGroup> list = new ArrayList<>();
    //                    YouTubeMediaGroup mediaGroup = new YouTubeMediaGroup(MediaGroup.TYPE_USER_PLAYLISTS);
    //                    mediaGroup.setTitle(tab.getTitle()); // id calculated by title hashcode
    //                    list.add(YouTubeMediaGroup.from(tabContinuation, mediaGroup));
    //                    emitter.onNext(list);
    //                }
    //            }
    //
    //            emitter.onComplete();
    //        } else {
    //            RxHelper.onError(emitter, "getPlaylistsObserve: tab is null");
    //        }
    //    });
    //}

    @Override
    public Observable<MediaGroup> getPlaylistsObserve() {
        return RxHelper.fromNullable(this::getPlaylists);
    }

    private MediaGroup getPlaylists() {
        checkSigned();

        return mBrowseService2.getMyPlaylists();
    }

    //@Override
    //public Observable<MediaGroup> getEmptyPlaylistsObserve() {
    //    return RxHelper.create(emitter -> {
    //        checkSigned();
    //
    //        List<GridTab> tabs = mBrowseService.getPlaylists();
    //
    //        if (tabs != null && tabs.size() > 0) {
    //            emitter.onNext(YouTubeMediaGroup.fromTabs(tabs, MediaGroup.TYPE_USER_PLAYLISTS));
    //            emitter.onComplete();
    //        } else {
    //            RxHelper.onError(emitter, "getEmptyPlaylistsObserve: tab is null");
    //        }
    //    });
    //}

    @Override
    public void enableHistory(boolean enable) {
        if (enable) {
            mActionsService.resumeWatchHistory();
        } else {
            mActionsService.pauseWatchHistory();
        }
    }

    @Override
    public void clearHistory() {
        mActionsService.clearWatchHistory();
    }

    @Override
    public void clearSearchHistory() {
        mActionsService.clearSearchHistory();
        mSearchService.clearSearchHistory();
    }

    //private Pair<List<ItemWrapper>, String> continueIfNeeded(List<ItemWrapper> items, String continuationKey) {
    //    List<ItemWrapper> combinedItems = items;
    //    String combinedKey = continuationKey;
    //
    //    for (int i = 0; i < 10; i++) {
    //        if (combinedKey == null || (combinedItems != null && combinedItems.size() > 60)) {
    //            break;
    //        }
    //
    //        GridTabContinuation result = mBrowseService.continueGridTab(combinedKey);
    //
    //        if (result != null) {
    //            List<ItemWrapper> newItems = result.getItemWrappers();
    //            if (newItems != null) {
    //                if (combinedItems == null) {
    //                    combinedItems = new ArrayList<>();
    //                }
    //                combinedItems.addAll(newItems);
    //            }
    //            combinedKey = result.getNextPageKey();
    //        }
    //    }
    //
    //    return new Pair<>(combinedItems, combinedKey);
    //}
}
