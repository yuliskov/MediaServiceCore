package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import io.reactivex.Observable;

import java.util.List;

public interface ContentService {
    List<MediaGroup> getSearch(String searchText);
    List<MediaGroup> getSearch(String searchText, int options);
    List<String> getSearchTags(String searchText);
    MediaGroup getSubscriptions();
    MediaGroup getRssFeed(String... channelIds);
    /**
     * NOTE: recommended is a part of Home
     */
    MediaGroup getRecommended();
    MediaGroup getHistory();
    List<MediaGroup> getHome();
    MediaGroup getSubscribedChannels();
    MediaGroup getSubscribedChannelsByNewContent();
    MediaGroup getSubscribedChannelsByName();
    MediaGroup getSubscribedChannelsByLastViewed();
    MediaGroup getGroup(MediaItem mediaItem);
    MediaGroup getChannelSearch(String channelId, String query);
    /**
     * NOTE: returns unnamed group
     */
    MediaGroup getGroup(String reloadPageKey);
    MediaGroup continueGroup(MediaGroup mediaGroup);
    void enableHistory(boolean enable);
    void clearHistory();
    void clearSearchHistory();

    // RxJava interfaces
    Observable<List<MediaGroup>> getSearchObserve(String searchText);
    Observable<List<MediaGroup>> getSearchObserve(String searchText, int options);
    Observable<List<String>> getSearchTagsObserve(String searchText);
    Observable<MediaGroup> getSubscriptionsObserve();
    Observable<MediaGroup> getRssFeedObserve(String... channelIds);
    /**
     * NOTE: recommended is a part of Home
     */
    Observable<MediaGroup> getRecommendedObserve();
    Observable<MediaGroup> getHistoryObserve();
    Observable<List<MediaGroup>> getHomeObserve();
    Observable<List<MediaGroup>> getTrendingObserve();
    Observable<MediaGroup> getShortsObserve();
    Observable<List<MediaGroup>> getKidsHomeObserve();
    Observable<List<MediaGroup>> getSportsObserve();
    Observable<List<MediaGroup>> getLiveObserve();
    Observable<MediaGroup> getMyVideosObserve();
    Observable<List<MediaGroup>> getMusicObserve();
    Observable<List<MediaGroup>> getNewsObserve();
    Observable<List<MediaGroup>> getGamingObserve();
    Observable<List<MediaGroup>> getChannelObserve(String channelId);
    Observable<List<MediaGroup>> getChannelObserve(MediaItem item);
    Observable<List<MediaGroup>> getChannelSortingObserve(String channelId);
    Observable<List<MediaGroup>> getChannelSortingObserve(MediaItem item);
    Observable<MediaGroup> getChannelSearchObserve(String channelId, String query);
    Observable<List<MediaGroup>> getPlaylistRowsObserve();
    Observable<MediaGroup> getPlaylistsObserve();
    Observable<MediaGroup> getSubscribedChannelsObserve();
    Observable<MediaGroup> getSubscribedChannelsByNewContentObserve();
    Observable<MediaGroup> getSubscribedChannelsByNameObserve();
    Observable<MediaGroup> getSubscribedChannelsByLastViewedObserve();
    Observable<MediaGroup> getGroupObserve(MediaItem mediaItem);
    /**
     * NOTE: returns unnamed group
     */
    Observable<MediaGroup> getGroupObserve(String reloadPageKey);
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup);
}
