package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.mediaserviceinterfaces.yt.data.Account;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
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
    public List<String> getSearchTags(String searchText) {
        if (isSearchTagsBroken()) {
            return getSearchTagStorage().getTags();
        }

        return super.getSearchTags(searchText);
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
