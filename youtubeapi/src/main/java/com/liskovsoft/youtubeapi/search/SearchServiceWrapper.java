package com.liskovsoft.youtubeapi.search;

import android.text.TextUtils;

import com.liskovsoft.mediaserviceinterfaces.yt.data.Account;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.YouTubeSignInService;
import com.liskovsoft.youtubeapi.service.data.YouTubeAccount;

import java.util.List;

public class SearchServiceWrapper extends SearchService {
    private static SearchServiceWrapper sInstance;
    private SearchTagStorage mSearchTagStorage;

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
            return getSearchTagStorage().getTags();
        }

        return super.getSearchTags(searchText);
    }

    private void saveTagIfNeeded(String searchText) {
        if (isSearchTagsBroken()) {
            getSearchTagStorage().saveTag(searchText);
        }
    }

    private boolean isSearchTagsBroken() {
        Account account = YouTubeSignInService.instance().getSelectedAccount();
        return account == null || ((YouTubeAccount) account).isSearchBroken();
    }

    private SearchTagStorage getSearchTagStorage() {
        if (mSearchTagStorage == null) {
            mSearchTagStorage = new SearchTagStorage(GlobalPreferences.sInstance.getContext(), YouTubeSignInService.instance());
        }

        return mSearchTagStorage;
    }
}
