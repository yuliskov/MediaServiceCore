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
    List<String> getSearchTags(String searchText);
    MediaGroup getSubscriptions();
    /**
     * NOTE: recommended is a part of Home
     */
    MediaGroup getRecommended();
    MediaGroup getHistory();
    List<MediaGroup> getHome();
    MediaGroup getSubscribedChannelsUpdate();
    MediaGroup getSubscribedChannelsAZ();
    MediaGroup getSubscribedChannelsLastViewed();
    MediaGroup getGroup(MediaItem mediaGroup);
    /**
     * NOTE: returns unnamed group
     */
    MediaGroup getGroup(String reloadPageKey);
    MediaGroup continueGroup(MediaGroup mediaGroup);

    // RxJava interfaces
    Observable<MediaGroup> getSearchObserve(String searchText);
    Observable<MediaGroup> getSearchObserve(String searchText, int options);
    Observable<List<MediaGroup>> getSearchAltObserve(String searchText);
    Observable<List<MediaGroup>> getSearchAltObserve(String searchText, int options);
    Observable<List<String>> getSearchTagsObserve(String searchText);
    Observable<MediaGroup> getSubscriptionsObserve();
    /**
     * NOTE: recommended is a part of Home
     */
    Observable<MediaGroup> getRecommendedObserve();
    Observable<MediaGroup> getHistoryObserve();
    Observable<List<MediaGroup>> getHomeObserve();
    Observable<List<MediaGroup>> getMusicObserve();
    Observable<List<MediaGroup>> getNewsObserve();
    Observable<List<MediaGroup>> getGamingObserve();
    Observable<List<MediaGroup>> getChannelObserve(String channelId);
    Observable<List<MediaGroup>> getChannelObserve(MediaItem item);
    Observable<List<MediaGroup>> getPlaylistsObserve();
    Observable<MediaGroup> getEmptyPlaylistsObserve();
    Observable<MediaGroup> getSubscribedChannelsUpdateObserve();
    Observable<MediaGroup> getSubscribedChannelsAZObserve();
    Observable<MediaGroup> getSubscribedChannelsLastViewedObserve();
    Observable<MediaGroup> getGroupObserve(MediaItem mediaItem);
    /**
     * NOTE: returns unnamed group
     */
    Observable<MediaGroup> getGroupObserve(String reloadPageKey);
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab);
}
