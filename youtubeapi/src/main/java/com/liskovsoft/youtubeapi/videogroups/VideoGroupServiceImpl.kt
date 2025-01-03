package com.liskovsoft.youtubeapi.videogroups

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.channelgroups.models.MediaItemGroupImpl
import com.liskovsoft.youtubeapi.service.internal.MediaServicePrefs
import io.reactivex.disposables.Disposable

internal object VideoGroupServiceImpl : MediaServicePrefs.ProfileChangeListener {
    private const val VIDEO_GROUP_DATA = "video_group_data"
    private lateinit var mPlaylists: MutableList<MediaItemGroup>
    private var mPersistAction: Disposable? = null

    init {
        MediaServicePrefs.addListener(this)
        restoreData()
    }

    override fun onProfileChanged() {
        restoreData()
    }

    private fun restoreData() {
        val data = MediaServicePrefs.getData(VIDEO_GROUP_DATA)
        restoreData(data)
    }

    private fun restoreData(data: String?) {
        val split = Helpers.splitData(data)

        mPlaylists = Helpers.parseList(split, 0, MediaItemGroupImpl::fromString)
        mPlaylists.forEach {
            it as MediaItemGroupImpl
            it.onChange = { persistData() }
        }
    }

    private fun persistData() {
        RxHelper.disposeActions(mPersistAction)
        mPersistAction = RxHelper.runAsync({ persistDataReal() }, 5_000)
    }

    private fun persistDataReal() {
        MediaServicePrefs.setData(VIDEO_GROUP_DATA, Helpers.mergeData(mPlaylists))
    }
}