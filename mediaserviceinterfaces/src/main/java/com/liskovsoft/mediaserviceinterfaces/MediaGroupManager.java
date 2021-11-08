package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import io.reactivex.Observable;

import java.util.List;

public interface MediaGroupManager {
    MediaGroup getSearch(String searchText);
    MediaGroup getSearch(String searchText, int options);
    List<String> getSearchTags(String searchText);
    MediaGroup getSubscriptions();
    MediaGroup getRecommended(); // Note, that recommended is a part of Home
    MediaGroup getHistory();
    List<MediaGroup> getHome();
    MediaGroup getSubscribedChannelsUpdate();
    MediaGroup getSubscribedChannelsAZ();
    MediaGroup getSubscribedChannelsLastViewed();
    MediaGroup getGroup(MediaItem mediaGroup);
    MediaGroup continueGroup(MediaGroup mediaGroup);

    // RxJava interfaces
    Observable<MediaGroup> getSearchObserve(String searchText);
    Observable<MediaGroup> getSearchObserve(String searchText, int options);
    Observable<List<String>> getSearchTagsObserve(String searchText);
    Observable<MediaGroup> getSubscriptionsObserve();
    Observable<MediaGroup> getRecommendedObserve(); // Note, that recommended is a part of Home
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
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab);
}
