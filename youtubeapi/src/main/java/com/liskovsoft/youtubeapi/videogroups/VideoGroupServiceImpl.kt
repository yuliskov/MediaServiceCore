package com.liskovsoft.youtubeapi.videogroups

import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.service.internal.MediaServicePrefs
import com.liskovsoft.youtubeapi.videogroups.models.VideoGroupImpl
import io.reactivex.disposables.Disposable

internal object VideoGroupServiceImpl : MediaServicePrefs.ProfileChangeListener {
    const val PLAYLIST_GROUP_ID: Int = 1000
    private const val VIDEO_GROUP_DATA = "channel_group_data"
    private lateinit var mVideoGroups: MutableList<VideoGroupImpl>
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

        mVideoGroups = Helpers.parseList(split, 0, VideoGroupImpl::fromString)
    }

    fun persistData() {
        RxHelper.disposeActions(mPersistAction)
        mPersistAction = RxHelper.runAsync({ persistDataReal() }, 5_000)
    }

    private fun persistDataReal() {
        MediaServicePrefs.setData(VIDEO_GROUP_DATA, Helpers.mergeData(mVideoGroups))
    }
}