package com.liskovsoft.youtubeapi.search

import android.content.Context
import com.liskovsoft.mediaserviceinterfaces.yt.SignInService.OnAccountChange
import com.liskovsoft.mediaserviceinterfaces.yt.data.Account
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase
import com.liskovsoft.youtubeapi.service.YouTubeSignInService

private const val PREF_NAME = "search_tag_storage"
private const val ANONYMOUS_PROFILE_NAME = "anonymous"
private const val SEARCH_TAG_DATA = "search_tag_data"

internal class SearchTagStorage(private val context: Context, private val signInService: YouTubeSignInService):
    SharedPreferencesBase(context, PREF_NAME), OnAccountChange {

    private var mProfileName: String? = null

    private val _tags: MutableList<String> = Helpers.createLRUList(50)
    val tags: List<String>
        get() = _tags.reversed()

    init {
        setProfileName(signInService.selectedAccount)
        signInService.addOnAccountChange(this)
        restoreData()
    }

    fun saveTag(tag: String?) {
        if (tag == null)
            return

        _tags.add(tag)

        persistData()
    }

    override fun onAccountChanged(account: Account?) {
        setProfileName(account)
        restoreData()
    }

    private fun setProfileName(account: Account?) {
        mProfileName = account?.name?.replace(" ", "_") ?: ANONYMOUS_PROFILE_NAME
    }

    private fun restoreData() {
        _tags.clear()

        val data = getSearchTagData()

        val split = Helpers.splitData(data)

        val tags = Helpers.parseStrList(split, 0)

        _tags.addAll(tags)
    }

    private fun persistData() {
        setSearchTagData(Helpers.mergeData(_tags))
    }

    private fun getSearchTagData(): String? {
        return getString(getSearchTagDataKey(), null)
    }

    private fun setSearchTagData(data: String?) {
        putString(getSearchTagDataKey(), data)
    }

    private fun getSearchTagDataKey() = if (mProfileName != null) "${mProfileName}_$SEARCH_TAG_DATA" else SEARCH_TAG_DATA
}