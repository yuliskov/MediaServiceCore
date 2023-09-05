package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import io.reactivex.Observable;

public interface NotificationsService {
    MediaGroup getNotificationItems();
    Observable<MediaGroup> getNotificationItemsObserve();
}
