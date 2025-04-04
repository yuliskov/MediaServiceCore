package com.liskovsoft.youtubeapi.search

import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.service.internal.MediaServicePrefs

internal object SearchTagStorage: MediaServicePrefs.ProfileChangeListener {
    private const val SEARCH_TAG_DATA = "search_tag_data"
    private val _tags: MutableList<String> = Helpers.createSafeLRUList(50)

    @JvmStatic
    val tags: List<String>
        get() = _tags.reversed()

    init {
        MediaServicePrefs.addListener(this)
        restoreData()
    }

    @JvmStatic
    fun saveTag(tag: String?) {
        if (tag == null)
            return

        _tags.add(tag)

        persistData()
    }

    @JvmStatic
    fun clear() {
        _tags.clear()

        persistData()
    }

    override fun onProfileChanged() {
        restoreData()
    }

    private fun restoreData() {
        _tags.clear()

        val data = MediaServicePrefs.getData(SEARCH_TAG_DATA)

        val split = Helpers.splitData(data)

        val tags = Helpers.parseStrList(split, 0)

        _tags.addAll(tags)
    }

    private fun persistData() {
        MediaServicePrefs.setData(SEARCH_TAG_DATA, Helpers.mergeData(_tags))
    }
}