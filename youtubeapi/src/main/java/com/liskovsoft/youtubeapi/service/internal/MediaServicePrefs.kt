package com.liskovsoft.youtubeapi.service.internal

import com.liskovsoft.mediaserviceinterfaces.yt.SignInService.OnAccountChange
import com.liskovsoft.mediaserviceinterfaces.yt.data.Account
import com.liskovsoft.sharedutils.misc.WeakHashSet
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase
import com.liskovsoft.youtubeapi.service.YouTubeSignInService

private const val PREF_NAME = "yt_service_prefs"

@Suppress("StaticFieldLeak")
internal object MediaServicePrefs: SharedPreferencesBase(GlobalPreferences.sInstance.context, PREF_NAME), OnAccountChange {
    private const val SEARCH_TAG_DATA = "search_tag_data"
    private const val ANONYMOUS_PROFILE_NAME = "anonymous"
    private val mListeners = WeakHashSet<ProfileChangeListener>()
    private lateinit var mProfileName: String

    interface ProfileChangeListener {
        fun onProfileChanged()
    }

    init {
        val signInService = YouTubeSignInService.instance()
        setProfileName(signInService.selectedAccount)
        signInService.addOnAccountChange(this)
    }

    override fun onAccountChanged(account: Account?) {
        setProfileName(account)
        notifyListeners()
    }

    private fun notifyListeners() {
        mListeners.forEach { it.onProfileChanged() }
    }

    private fun setProfileName(account: Account?) {
        mProfileName = account?.name?.replace(" ", "_") ?: ANONYMOUS_PROFILE_NAME
    }

    fun addListener(listener: ProfileChangeListener) {
        mListeners.add(listener)
    }

    fun getSearchTagData(): String? {
        return getString(getProfileDataKey(SEARCH_TAG_DATA), null)
    }

    fun setSearchTagData(data: String?) {
        putString(getProfileDataKey(SEARCH_TAG_DATA), data)
    }

    private fun getProfileDataKey(dataKey: String) = "${mProfileName}_$dataKey"
}