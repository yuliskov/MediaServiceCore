package com.liskovsoft.youtubeapi.search

import android.content.Context
import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase

private val PREF_NAME = SearchTagStorage::class.java.simpleName

internal class SearchTagStorage(context: Context): SharedPreferencesBase(context, PREF_NAME) {
}