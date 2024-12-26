package com.liskovsoft.youtubeapi.channelgroups

import android.net.Uri
import com.liskovsoft.mediaserviceinterfaces.yt.ChannelGroupService
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.channelgroups.importing.grayjay.GrayJayService
import com.liskovsoft.youtubeapi.channelgroups.importing.pockettube.PocketTubeService
import com.liskovsoft.youtubeapi.channelgroups.models.ChannelGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.ChannelImpl
import com.liskovsoft.youtubeapi.service.internal.MediaServicePrefs
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

internal object ChannelGroupServiceImpl: MediaServicePrefs.ProfileChangeListener, ChannelGroupService {
    const val SUBSCRIPTION_GROUP_ID: Int = 1000
    private const val CHANNEL_GROUP_DATA = "channel_group_data"
    private val mImportServices = listOf(PocketTubeService, GrayJayService)
    private lateinit var mChannelGroups: MutableList<ChannelGroup>
    private var mPersistAction: Disposable? = null

    init {
        MediaServicePrefs.addListener(this)
        restoreData()
    }

    override fun onProfileChanged() {
        restoreData()
    }

    override fun getChannelGroups(): List<ChannelGroup> {
        return mChannelGroups
    }

    override fun addChannelGroup(group: ChannelGroup) {
        // Move to the top
        mChannelGroups.remove(group)
        mChannelGroups.add(0, group)
        persistData()
    }

    override fun removeChannelGroup(group: ChannelGroup) {
        if (mChannelGroups.contains(group)) {
            mChannelGroups.remove(group)
            persistData()
        }
    }

    override fun findChannelGroup(channelGroupId: Int): ChannelGroup? {
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

    override fun findChannelGroup(title: String): ChannelGroup? {
        for (group in mChannelGroups) {
            if (group.title == title) {
                return group
            }
        }

        return null
    }

    override fun getSubscribedChannelGroup(): ChannelGroup? {
        return findChannelGroup(SUBSCRIPTION_GROUP_ID)
    }

    override fun findChannelIdsForGroup(channelGroupId: Int): Array<String>? {
        if (channelGroupId == -1) {
            return null
        }

        val result: MutableList<String> = ArrayList()

        var channelGroup: ChannelGroup? = null

        for (group in mChannelGroups) {
            if (group.id == channelGroupId) {
                channelGroup = group
                break
            }
        }

        channelGroup?.let {
            for (channel in it.channels) {
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

    override fun importGroupsObserve(uri: Uri): Observable<List<ChannelGroup>> {
        return RxHelper.fromCallable { importGroupsReal(uri) }
    }

    override fun createChannelGroup(title: String, iconUrl: String?, channels: MutableList<ChannelGroup.Channel>): ChannelGroup {
        return ChannelGroupImpl(title = title, iconUrl = iconUrl, channels = channels)
    }

    override fun renameChannelGroup(channelGroup: ChannelGroup, title: String) {
        addChannelGroup(ChannelGroupImpl(channelGroup.id, title, channelGroup.iconUrl, channelGroup.channels))
    }

    override fun createChannel(title: String?, iconUrl: String?, channelId: String): ChannelGroup.Channel {
        return ChannelImpl(title = title, iconUrl = iconUrl, channelId = channelId)
    }

    private fun importGroupsReal(uri: Uri): List<ChannelGroup>? {
        val groups = mImportServices.firstNotNullOfOrNull { it.importGroups(uri) } ?: return null
        val result = mutableListOf<ChannelGroup>()

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

        persistData()

        return result
    }

    override fun exportData(data: String?) {
        data?.let {
            restoreData(it)
            persistData()
        }
    }

    fun subscribe(title: String?, iconUrl: String?, channelId: String, subscribe: Boolean) {
        val group: ChannelGroup = findChannelGroup(SUBSCRIPTION_GROUP_ID) ?:
            ChannelGroupImpl(SUBSCRIPTION_GROUP_ID, "Subscriptions", null, mutableListOf())

        if (subscribe) {
            group.add(ChannelImpl(title, iconUrl, channelId))
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
        val group: ChannelGroup? = findChannelGroup(SUBSCRIPTION_GROUP_ID)

        return group?.contains(channelId) ?: false
    }

    private fun restoreData() {
        val data = MediaServicePrefs.getData(CHANNEL_GROUP_DATA)
        restoreData(data)
    }

    private fun restoreData(data: String?) {
        val split = Helpers.splitData(data)

        mChannelGroups = Helpers.parseList(split, 0, ChannelGroupImpl::fromString)
    }

    fun persistData() {
        RxHelper.disposeActions(mPersistAction)
        mPersistAction = RxHelper.runAsync({ persistDataReal() }, 5_000)
    }

    private fun persistDataReal() {
        MediaServicePrefs.setData(CHANNEL_GROUP_DATA, Helpers.mergeData(mChannelGroups))
    }
}