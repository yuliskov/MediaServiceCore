package com.liskovsoft.youtubeapi.search

import android.content.Context
import com.liskovsoft.mediaserviceinterfaces.yt.SignInService.OnAccountChange
import com.liskovsoft.mediaserviceinterfaces.yt.data.Account
import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase
import com.liskovsoft.youtubeapi.service.YouTubeSignInService

private val PREF_NAME = SearchTagStorage::class.simpleName
private const val ANONYMOUS_PROFILE_NAME = "anonymous"
private const val SEARCH_TAG_DATA = "search_tag_data"

internal class SearchTagStorage(private val context: Context, private val signInService: YouTubeSignInService):
    SharedPreferencesBase(context, PREF_NAME), OnAccountChange {

    private var mProfileName: String? = null

    init {
        setProfileName(signInService.selectedAccount)
        signInService.addOnAccountChange(this)
    }

    val tags: List<String> = TODO("initialize me")

    override fun onAccountChanged(account: Account?) {
        setProfileName(account)
        restoreData()
    }

    private fun setProfileName(account: Account?) {
        mProfileName = account?.name?.replace(" ", "_") ?: ANONYMOUS_PROFILE_NAME
    }

    private fun restoreData() {
        TODO("Not yet implemented")
    }

    private fun persistData() {
        
    }

    private fun getSearchTagData(): String? {
        return getString(getSearchTagDataKey(), null)
    }

    private fun setSearchTagData(data: String?) {
        putString(getSearchTagDataKey(), data)
    }

    private fun getSearchTagDataKey() = if (mProfileName != null) "${mProfileName}_$SEARCH_TAG_DATA" else SEARCH_TAG_DATA
}