package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.search.models.SearchResult;

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
        SearchTagStorage.saveTag(searchText);

        return super.getSearch(searchText);
    }

    @Override
    public SearchResult getSearch(String searchText, int options) {
        SearchTagStorage.saveTag(searchText);

        return super.getSearch(searchText, options);
    }

    @Override
    public List<String> getSearchTags(String searchText) {
        List<String> result = super.getSearchTags(searchText);

        if (result == null || result.isEmpty()) {
            return SearchTagStorage.getTags();
        }

        return result;
    }

    @Override
    public void clearSearchHistory() {
        SearchTagStorage.clear();
    }
}
