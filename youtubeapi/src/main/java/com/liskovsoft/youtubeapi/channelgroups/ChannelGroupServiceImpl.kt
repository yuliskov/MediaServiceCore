package com.liskovsoft.youtubeapi.channelgroups

import android.net.Uri
import com.liskovsoft.mediaserviceinterfaces.yt.ChannelGroupService
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup.MediaItem
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.channelgroups.importing.grayjay.GrayJayService
import com.liskovsoft.youtubeapi.channelgroups.importing.newpipe.NewPipeService
import com.liskovsoft.youtubeapi.channelgroups.importing.pockettube.PocketTubeService
import com.liskovsoft.youtubeapi.channelgroups.models.MediaItemGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.MediaItemImpl
import com.liskovsoft.youtubeapi.service.internal.MediaServicePrefs
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.File

internal object ChannelGroupServiceImpl: MediaServicePrefs.ProfileChangeListener, ChannelGroupService {
    const val SUBSCRIPTION_GROUP_ID: Int = 1000
    private const val CHANNEL_GROUP_DATA = "channel_group_data"
    private val mImportServices = listOf(PocketTubeService, GrayJayService, NewPipeService)
    private lateinit var mChannelGroups: MutableList<MediaItemGroup>
    private var mPersistAction: Disposable? = null
    var cachedChannel: MediaItem? = null

    init {
        MediaServicePrefs.addListener(this)
        restoreData()
    }

    override fun onProfileChanged() {
        restoreData()
    }

    override fun getChannelGroups(): List<MediaItemGroup> {
        return mChannelGroups
    }

    override fun addChannelGroup(group: MediaItemGroup) {
        // Move to the top
        mChannelGroups.remove(group)
        mChannelGroups.add(0, group)
        persistData()
    }

    override fun removeChannelGroup(group: MediaItemGroup?) {
        if (mChannelGroups.contains(group)) {
            mChannelGroups.remove(group)
            persistData()
        }
    }

    override fun findChannelGroup(channelGroupId: Int): MediaItemGroup? {
        if (channelGroupId == -1) {
            return null
        }

        for (group in mChannelGroups) {
            if (group.id == channelGroupId) {
                return group
            }
        }

        return null
    }

    override fun findChannelGroup(title: String): MediaItemGroup? {
        for (group in mChannelGroups) {
            if (group.title == title) {
                return group
            }
        }

        return null
    }

    override fun getSubscribedChannelGroup(): MediaItemGroup? {
        return findChannelGroup(SUBSCRIPTION_GROUP_ID)
    }

    override fun findChannelIdsForGroup(channelGroupId: Int): Array<String>? {
        if (channelGroupId == -1) {
            return null
        }

        val result: MutableList<String> = ArrayList()

        var mediaItemGroup: MediaItemGroup? = null

        for (group in mChannelGroups) {
            if (group.id == channelGroupId) {
                mediaItemGroup = group
                break
            }
        }

        mediaItemGroup?.let {
            for (channel in it.mediaItems) {
                result.add(channel.channelId)
            }
        }

        return result.toTypedArray()
    }

    override fun getSubscribedChannelIds(): Array<String>? {
        return findChannelIdsForGroup(SUBSCRIPTION_GROUP_ID)
    }

    override fun isEmpty(): Boolean {
        return mChannelGroups.isEmpty()
    }

    override fun importGroupsObserve(uri: Uri): Observable<List<MediaItemGroup>> {
        return RxHelper.fromCallable { importGroupsReal(uri) }
    }

    override fun importGroupsObserve(file: File): Observable<List<MediaItemGroup>> {
        return RxHelper.fromCallable { importGroupsReal(file) }
    }

    override fun createChannelGroup(title: String, iconUrl: String?, mediaItems: List<MediaItem>): MediaItemGroup {
        return MediaItemGroupImpl(title = title, iconUrl = iconUrl, mediaItems = mediaItems.toMutableList())
    }

    override fun renameChannelGroup(mediaItemGroup: MediaItemGroup, title: String) {
        addChannelGroup(MediaItemGroupImpl(mediaItemGroup.id, title, mediaItemGroup.iconUrl, mediaItemGroup.mediaItems))
    }

    override fun createChannel(title: String?, iconUrl: String?, channelId: String): MediaItem {
        return MediaItemImpl(channelId = channelId, title = title, iconUrl = iconUrl)
    }

    private fun importGroupsReal(uri: Uri): List<MediaItemGroup>? {
        val groups = mImportServices.firstNotNullOfOrNull { it.importGroups(uri) } ?: return null
        return persistGroups(groups)
    }

    private fun importGroupsReal(file: File): List<MediaItemGroup>? {
        val groups = mImportServices.firstNotNullOfOrNull {
            val result = it.importGroups(file)
            if (it is NewPipeService) {
                // NewPipe can export only subscribed channels
                result?.firstOrNull()?.mediaItems?.let {
                    subscribedChannelGroup?.addAll(it)
                }
            }
            result
        } ?: return null
        return persistGroups(groups)
    }

    private fun persistGroups(groups: List<MediaItemGroup>): List<MediaItemGroup> {
        val result = mutableListOf<MediaItemGroup>()

        groups.forEach {
            //val idx = mChannelGroups?.indexOf(it) ?: -1
            val contains = Helpers.containsIf(mChannelGroups) { item -> item.title == it.title }
            if (contains) { // already exists
                //mChannelGroups?.add(ChannelGroupImpl(title = "${it.title} 2", iconUrl = it.iconUrl, channels = it.channels))
                return@forEach
            }

            mChannelGroups.add(it)
            result.add(it)
        }

        if (result.isNotEmpty()) {
            persistData()
        }

        return result
    }

    override fun exportData(data: String?) {
        data?.let {
            restoreData(it)
            persistData()
        }
    }

    fun subscribe(title: String?, iconUrl: String?, channelId: String, subscribe: Boolean) {
        val group: MediaItemGroup = findChannelGroup(SUBSCRIPTION_GROUP_ID) ?:
            MediaItemGroupImpl(SUBSCRIPTION_GROUP_ID, "Subscriptions", null, mutableListOf())

        if (subscribe) {
            val realCachedChannel = cachedChannel
            val newChannel = if (channelId == realCachedChannel?.channelId)
                realCachedChannel
            else MediaItemImpl(channelId, title, iconUrl)
            group.add(newChannel)
        } else {
            group.remove(channelId)
        }

        if (!group.isEmpty()) {
            addChannelGroup(group)
        } else {
            removeChannelGroup(group)
        }
    }

    fun isSubscribed(channelId: String): Boolean {
        val group: MediaItemGroup? = findChannelGroup(SUBSCRIPTION_GROUP_ID)

        return group?.contains(channelId) ?: false
    }

    private fun restoreData() {
        val data = MediaServicePrefs.getData(CHANNEL_GROUP_DATA)
        restoreData(data)
    }

    private fun restoreData(data: String?) {
        val split = Helpers.splitData(data)

        mChannelGroups = Helpers.parseList(split, 0, MediaItemGroupImpl::fromString)
    }

    fun persistData() {
        RxHelper.disposeActions(mPersistAction)
        mPersistAction = RxHelper.runAsync({ persistDataReal() }, 5_000)
    }

    private fun persistDataReal() {
        MediaServicePrefs.setData(CHANNEL_GROUP_DATA, Helpers.mergeData(mChannelGroups))
    }
}