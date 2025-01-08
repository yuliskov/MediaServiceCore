package com.liskovsoft.youtubeapi.channelgroups

import android.net.Uri
import com.liskovsoft.mediaserviceinterfaces.yt.ChannelGroupService
import com.liskovsoft.mediaserviceinterfaces.yt.data.ItemGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.ItemGroup.Item
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.channelgroups.importing.grayjay.GrayJayService
import com.liskovsoft.youtubeapi.channelgroups.importing.newpipe.NewPipeService
import com.liskovsoft.youtubeapi.channelgroups.importing.pockettube.PocketTubeService
import com.liskovsoft.youtubeapi.channelgroups.models.ItemGroupImpl
import com.liskovsoft.youtubeapi.channelgroups.models.ItemImpl
import com.liskovsoft.youtubeapi.service.internal.MediaServicePrefs
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.File

internal object ChannelGroupServiceImpl: MediaServicePrefs.ProfileChangeListener, ChannelGroupService {
    const val SUBSCRIPTION_GROUP_ID: Int = 1000
    const val SUBSCRIPTION_GROUP_NAME: String = "Subscriptions"
    private const val CHANNEL_GROUP_DATA = "channel_group_data"
    private val mImportServices = listOf(PocketTubeService, GrayJayService, NewPipeService)
    private lateinit var mChannelGroups: MutableList<ItemGroup>
    private var mPersistAction: Disposable? = null
    var cachedChannel: Item? = null

    init {
        MediaServicePrefs.addListener(this)
        restoreData()
    }

    override fun onProfileChanged() {
        restoreData()
    }

    override fun getChannelGroups(): List<ItemGroup> {
        return mChannelGroups
    }

    override fun addChannelGroup(group: ItemGroup) {
        // Move to the top
        mChannelGroups.remove(group)
        mChannelGroups.add(0, group)
        persistData()
    }

    override fun removeChannelGroup(group: ItemGroup?) {
        if (mChannelGroups.contains(group)) {
            mChannelGroups.remove(group)
            persistData()
        }
    }

    override fun findChannelGroup(channelGroupId: Int): ItemGroup? {
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

    override fun findChannelGroup(title: String): ItemGroup? {
        for (group in mChannelGroups) {
            if (group.title == title) {
                return group
            }
        }

        return null
    }

    fun backupSubscribedChannels(subscribedChannels: MediaGroup) {
        val items = subscribedChannels.mediaItems?.map {
            ItemImpl(it.channelId, it.title, it.cardImageUrl, it.videoId, it.secondTitle, it.reloadPageKey)
        } ?: return
        val group = ItemGroupImpl(SUBSCRIPTION_GROUP_ID, SUBSCRIPTION_GROUP_NAME, null, items.toMutableList())
        addChannelGroup(group)
    }

    fun getSubscribedChannelGroup(): ItemGroup? {
        return findChannelGroup(SUBSCRIPTION_GROUP_ID)
    }

    fun getSubscribedChannelIds(): Array<String>? {
        return findChannelIdsForGroup(SUBSCRIPTION_GROUP_ID)
    }

    override fun findChannelIdsForGroup(channelGroupId: Int): Array<String>? {
        if (channelGroupId == -1) {
            return null
        }

        val result: MutableList<String> = ArrayList()

        var itemGroup: ItemGroup? = null

        for (group in mChannelGroups) {
            if (group.id == channelGroupId) {
                itemGroup = group
                break
            }
        }

        itemGroup?.let {
            for (channel in it.items) {
                channel.channelId?.let { result.add(it) }
            }
        }

        return result.toTypedArray()
    }

    override fun isEmpty(): Boolean {
        return mChannelGroups.isEmpty()
    }

    override fun importGroupsObserve(uri: Uri): Observable<List<ItemGroup>> {
        return RxHelper.fromCallable { importGroupsReal(uri) }
    }

    override fun importGroupsObserve(file: File): Observable<List<ItemGroup>> {
        return RxHelper.fromCallable { importGroupsReal(file) }
    }

    override fun createChannelGroup(title: String, iconUrl: String?, items: List<Item>): ItemGroup {
        return ItemGroupImpl(title = title, iconUrl = iconUrl, items = items.toMutableList())
    }

    override fun renameChannelGroup(itemGroup: ItemGroup, title: String) {
        addChannelGroup(ItemGroupImpl(itemGroup.id, title, itemGroup.iconUrl, itemGroup.items))
    }

    override fun createChannel(title: String?, iconUrl: String?, channelId: String): Item {
        return ItemImpl(channelId = channelId, title = title, iconUrl = iconUrl)
    }

    private fun importGroupsReal(uri: Uri): List<ItemGroup>? {
        val groups = mImportServices.firstNotNullOfOrNull { it.importGroups(uri) } ?: return null
        return persistGroups(groups)
    }

    private fun importGroupsReal(file: File): List<ItemGroup>? {
        val groups = mImportServices.firstNotNullOfOrNull {
            val result = it.importGroups(file)
            if (it is NewPipeService) {
                // NewPipe can export only subscribed channels
                result?.firstOrNull()?.items?.let {
                    getSubscribedChannelGroup()?.addAll(it)
                }
            }
            result
        } ?: return null
        return persistGroups(groups)
    }

    private fun persistGroups(groups: List<ItemGroup>): List<ItemGroup> {
        val result = mutableListOf<ItemGroup>()

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

    @JvmStatic
    fun subscribe(title: String?, iconUrl: String?, channelId: String, subscribe: Boolean) {
        val group: ItemGroup = findChannelGroup(SUBSCRIPTION_GROUP_ID) ?:
            ItemGroupImpl(SUBSCRIPTION_GROUP_ID, SUBSCRIPTION_GROUP_NAME, null, mutableListOf())

        if (subscribe) {
            val realCachedChannel = cachedChannel
            val newChannel = if (channelId == realCachedChannel?.channelId)
                realCachedChannel
            else ItemImpl(channelId, title, iconUrl)
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
        val group: ItemGroup? = findChannelGroup(SUBSCRIPTION_GROUP_ID)

        return group?.contains(channelId) ?: false
    }

    private fun restoreData() {
        val data = MediaServicePrefs.getData(CHANNEL_GROUP_DATA)
        restoreData(data)
    }

    private fun restoreData(data: String?) {
        val split = Helpers.splitData(data)

        mChannelGroups = Helpers.parseList(split, 0, ItemGroupImpl::fromString)
    }

    fun persistData() {
        RxHelper.disposeActions(mPersistAction)
        mPersistAction = RxHelper.runAsync({ persistDataReal() }, 5_000)
    }

    private fun persistDataReal() {
        MediaServicePrefs.setData(CHANNEL_GROUP_DATA, Helpers.mergeData(mChannelGroups))
    }
}