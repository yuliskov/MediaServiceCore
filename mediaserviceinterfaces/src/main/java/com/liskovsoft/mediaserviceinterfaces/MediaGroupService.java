package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import io.reactivex.Observable;

import java.util.List;

public interface MediaGroupService {
    MediaGroup getSearch(String searchText);
    MediaGroup getSearch(String searchText, int options);
    List<MediaGroup> getSearchAlt(String searchText);
    List<MediaGroup> getSearchAlt(String searchText, int options);
    List<String> getSearchTags(String searchText, boolean popular);
    MediaGroup getSubscriptions();
    /**
     * NOTE: recommended is a part of Home
     */
    MediaGroup getRecommended();
    MediaGroup getHistory();
    List<MediaGroup> getHome();
    MediaGroup getSubscribedChannels();
    MediaGroup getSubscribedChannelsByUpdate();
    MediaGroup getSubscribedChannelsByName();
    MediaGroup getSubscribedChannelsByName2();
    MediaGroup getSubscribedChannelsByViewed();
    MediaGroup getGroup(MediaItem mediaGroup);
    /**
     * NOTE: returns unnamed group
     */
    MediaGroup getGroup(String reloadPageKey);
    MediaGroup continueGroup(MediaGroup mediaGroup);
    void enableHistory(boolean enable);
    void clearHistory();
    void clearSearchHistory();

    // RxJava interfaces
    Observable<MediaGroup> getSearchObserve(String searchText);
    Observable<MediaGroup> getSearchObserve(String searchText, int options);
    Observable<List<MediaGroup>> getSearchAltObserve(String searchText);
    Observable<List<MediaGroup>> getSearchAltObserve(String searchText, int options);
    Observable<List<String>> getSearchTagsObserve(String searchText, boolean popular);
    Observable<MediaGroup> getSubscriptionsObserve();
    /**
     * NOTE: recommended is a part of Home
     */
    Observable<MediaGroup> getRecommendedObserve();
    Observable<MediaGroup> getHistoryObserve();
    Observable<List<MediaGroup>> getHomeV1Observe();
    Observable<List<MediaGroup>> getHomeObserve();
    Observable<List<MediaGroup>> getTrendingObserve();
    Observable<MediaGroup> getShortsObserve();
    Observable<List<MediaGroup>> getKidsHomeObserve();
    Observable<List<MediaGroup>> getMusicObserve();
    Observable<List<MediaGroup>> getNewsObserve();
    Observable<List<MediaGroup>> getGamingObserve();
    Observable<List<MediaGroup>> getChannelObserve(String channelId);
    Observable<List<MediaGroup>> getChannelObserve(MediaItem item);
    Observable<List<MediaGroup>> getPlaylistsObserve();
    Observable<MediaGroup> getEmptyPlaylistsObserve();
    Observable<MediaGroup> getSubscribedChannelsObserve();
    Observable<MediaGroup> getSubscribedChannelsByUpdateObserve();
    Observable<MediaGroup> getSubscribedChannelsByNameObserve();
    Observable<MediaGroup> getSubscribedChannelsByName2Observe();
    Observable<MediaGroup> getSubscribedChannelsByViewedObserve();
    Observable<MediaGroup> getGroupObserve(MediaItem mediaItem);
    /**
     * NOTE: returns unnamed group
     */
    Observable<MediaGroup> getGroupObserve(String reloadPageKey);
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup);
}
