package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.NotificationState;
import io.reactivex.Observable;

public interface NotificationsService {
    MediaGroup getNotificationItems();
    void hideNotification(MediaItem item);
    void setNotificationState(NotificationState state);

    Observable<MediaGroup> getNotificationItemsObserve();
    Observable<Void> hideNotificationObserve(MediaItem item);
    Observable<Void> setNotificationStateObserve(NotificationState state);
}
