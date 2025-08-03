package com.liskovsoft.youtubeapi.search;

import android.text.TextUtils;

import com.liskovsoft.mediaserviceinterfaces.oauth.Account;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.YouTubeSignInService;
import com.liskovsoft.googlecommon.service.oauth.YouTubeAccount;

import java.util.List;

public class SearchServiceWrapper extends SearchService {
    private static SearchServiceWrapper sInstance;

    public static SearchServiceWrapper instance() {
        if (sInstance == null) {
            sInstance = new SearchServiceWrapper();
        }

        return sInstance;
    }

    @Override
    public SearchResult getSearch(String searchText) {
        saveTagIfNeeded(searchText);

        return super.getSearch(searchText);
    }

    @Override
    public SearchResult getSearch(String searchText, int options) {
        saveTagIfNeeded(searchText);

        return super.getSearch(searchText, options);
    }

    @Override
    public List<String> getSearchTags(String searchText) {
        if (TextUtils.isEmpty(searchText) && isSearchTagsBroken()) {
            return SearchTagStorage.getTags();
        }

        return super.getSearchTags(searchText);
    }

    @Override
    public void clearSearchHistory() {
        SearchTagStorage.clear();
    }

    private void saveTagIfNeeded(String searchText) {
        if (isSearchTagsBroken()) {
            SearchTagStorage.saveTag(searchText);
        }
    }

    private boolean isSearchTagsBroken() {
        if (GlobalPreferences.sInstance == null) {
            return false;
        }

        Account account = YouTubeSignInService.instance().getSelectedAccount();
        return account == null || ((YouTubeAccount) account).isSearchBroken();
    }
}
