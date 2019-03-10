package com.liskovsoft.youtubeapi.adapters;

import com.liskovsoft.myvideotubeapi.Video;
import com.liskovsoft.myvideotubeapi.VideoService;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.search.SearchManagerHelper;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeVideoService implements VideoService {
    private final SearchManagerHelper mSearchHelper;

    public YouTubeVideoService() {
        mSearchHelper = new SearchManagerHelper();
    }

    @Override
    public Observable<List<Video>> findVideos(String searchText) {
        return null;
    }

    private List<VideoItem> findVideoItems(String searchText) {
        SearchResult searchResult = mSearchHelper.startSearch(searchText);

        return searchResult.getVideoItems();
    }

    private List<Video> convertVideoItems(List<VideoItem> items) {
        return null;
    }
}
