package com.liskovsoft.youtubeapi.common.models.impl

import com.liskovsoft.mediaserviceinterfaces.data.NotificationState
import com.liskovsoft.youtubeapi.common.models.gen.NotificationStateItem
import com.liskovsoft.youtubeapi.common.models.gen.getStateId
import com.liskovsoft.youtubeapi.common.models.gen.getStateParams
import com.liskovsoft.youtubeapi.common.models.gen.getTitle

data class NotificationStateImpl(
    val notificationStateItem: NotificationStateItem,
    val selectedSateId: Int?
): NotificationState {
    override fun isSelected(): Boolean {
        return notificationStateItem.getStateId() == selectedSateId
    }

    override fun getTitle(): String? {
        return notificationStateItem.getTitle()
    }
    val stateParams = notificationStateItem.getStateParams()
}
