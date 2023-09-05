package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import io.reactivex.Observable;

public interface NotificationsService {
    MediaGroup getNotificationItems();
    void hideNotification(MediaItem item);

    Observable<MediaGroup> getNotificationItemsObserve();
    Observable<Void> hideNotificationObserve(MediaItem item);
}
