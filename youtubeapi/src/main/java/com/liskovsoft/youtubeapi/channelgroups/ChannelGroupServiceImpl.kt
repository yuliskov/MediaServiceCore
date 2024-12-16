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

internal object ChannelGroupServiceImpl: MediaServicePrefs.ProfileChangeListener, ChannelGroupService {
    const val SUBSCRIPTION_GROUP_ID: Int = 1000
    private const val CHANNEL_GROUP_DATA = "channel_group_data"
    private val mImportServices = listOf(PocketTubeService, GrayJayService)
    private var mChannelGroups: MutableList<ChannelGroup>? = null

    init {
        MediaServicePrefs.addListener(this)
        restoreData()
    }

    override fun onProfileChanged() {
        restoreData()
    }

    override fun getChannelGroups(): List<ChannelGroup>? {
        return mChannelGroups
    }

    override fun addChannelGroup(group: ChannelGroup) {
        mChannelGroups?.let {
            // Move to the top
            it.remove(group)
            it.add(0, group)
            persistData()
        }
    }

    override fun removeChannelGroup(group: ChannelGroup) {
        mChannelGroups?.let {
            if (it.contains(group)) {
                it.remove(group)
                persistData()
            }
        }
    }

    override fun findChannelGroup(channelGroupId: Int): ChannelGroup? {
        if (channelGroupId == -1) {
            return null
        }

        mChannelGroups?.let {
            for (group in it) {
                if (group.id == channelGroupId) {
                    return group
                }
            }
        }

        return null
    }

    override fun findChannelGroup(title: String?): ChannelGroup? {
        if (title == null) {
            return null
        }

        mChannelGroups?.let {
            for (group in it) {
                if (Helpers.equals(group.title, title)) {
                    return group
                }
            }
        }

        return null
    }

    override fun getChannelGroupIds(channelGroupId: Int): Array<String>? {
        if (channelGroupId == -1) {
            return null
        }

        val result: MutableList<String> = ArrayList()

        var channelGroup: ChannelGroup? = null

        mChannelGroups?.let {
            for (group in it) {
                if (group.id == channelGroupId) {
                    channelGroup = group
                    break
                }
            }
        }

        channelGroup?.let {
            for (channel in it.channels) {
                result.add(channel.channelId)
            }
        }

        return result.toTypedArray()
    }

    override fun isEmpty(): Boolean {
        return mChannelGroups.isNullOrEmpty()
    }

    override fun importGroups(uri: Uri): Observable<Void> {
        return RxHelper.fromVoidable { importGroupsReal(uri) }
    }

    private fun importGroupsReal(uri: Uri) {
        val groups = mImportServices.firstNotNullOfOrNull { it.importGroups(uri) } ?: return

        groups.forEach {
            val idx = mChannelGroups?.indexOf(it) ?: -1
            if (idx != -1) { // already exists
                mChannelGroups?.add(ChannelGroupImpl(title = "${it.title} 2", iconUrl = it.iconUrl, channels = it.channels))

                //val existingGroup = mChannelGroups?.get(idx)
                //it.channels.forEach {
                //    if (existingGroup?.contains(it.channelId) == true)
                //        return@forEach
                //    existingGroup?.add(it)
                //}
                return@forEach
            }

            mChannelGroups?.add(it)
        }

        persistData()
    }

    override fun exportData(data: String?) {
        data?.let {
            restoreData(it)
        }
    }

    fun subscribe(title: String, iconUrl: String?, channelId: String, subscribe: Boolean) {
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

    fun isSubscribed(channelId: String?): Boolean {
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

    private fun persistData() {
        MediaServicePrefs.setData(CHANNEL_GROUP_DATA, Helpers.mergeData(mChannelGroups))
    }
}