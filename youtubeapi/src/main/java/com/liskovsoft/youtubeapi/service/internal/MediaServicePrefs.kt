package com.liskovsoft.youtubeapi.service.internal

import com.liskovsoft.mediaserviceinterfaces.SignInService.OnAccountChange
import com.liskovsoft.mediaserviceinterfaces.oauth.Account
import com.liskovsoft.sharedutils.misc.WeakHashSet
import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.service.YouTubeSignInService

private const val PREF_NAME = "yt_service_prefs"

internal object MediaServicePrefs: SharedPreferencesBase(AppService.instance().context, PREF_NAME), OnAccountChange {
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

    fun getData(key: String): String? {
        return getString(getProfileDataKey(key), null)
    }

    fun setData(key: String, data: String?) {
        putString(getProfileDataKey(key), data)
    }

    private fun getProfileDataKey(dataKey: String) = "${mProfileName}_$dataKey"
}