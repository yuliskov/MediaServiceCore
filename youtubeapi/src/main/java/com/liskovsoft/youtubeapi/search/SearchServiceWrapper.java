package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.mediaserviceinterfaces.yt.data.Account;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.service.YouTubeSignInService;
import com.liskovsoft.youtubeapi.service.data.YouTubeAccount;

import java.util.List;

public class SearchServiceWrapper {
    private static SearchServiceWrapper sInstance;
    private final SearchService mSearchService;
    private SearchTagStorage mSearchTagStorage;

    private SearchServiceWrapper() {
        mSearchService = SearchService.instance();
    }

    public static SearchServiceWrapper instance() {
        if (sInstance == null) {
            sInstance = new SearchServiceWrapper();
        }

        return sInstance;
    }

    public List<String> getSearchTags(String searchText) {
        if (isSearchTagsBroken()) {
            return getSearchTagStorage().getTags();
        }

        return mSearchService.getSearchTags(searchText);
    }

    private boolean isSearchTagsBroken() {
        Account account = YouTubeSignInService.instance().getSelectedAccount();
        return account != null && ((YouTubeAccount) account).isSearchBroken();
    }

    private SearchTagStorage getSearchTagStorage() {
        if (mSearchTagStorage == null) {
            mSearchTagStorage = new SearchTagStorage(GlobalPreferences.sInstance.getContext(), YouTubeSignInService.instance());
        }

        return mSearchTagStorage;
    }
}
