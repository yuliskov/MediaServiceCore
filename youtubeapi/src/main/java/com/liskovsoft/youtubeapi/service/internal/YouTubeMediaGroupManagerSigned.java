package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.search.SearchServiceSigned;
import com.liskovsoft.youtubeapi.search.SearchServiceUnsigned;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;
import com.liskovsoft.youtubeapi.service.YouTubeSignInManager;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;

import java.util.List;

public class YouTubeMediaGroupManagerSigned implements MediaGroupManagerInt {
    private static final String TAG = YouTubeMediaGroupManagerSigned.class.getSimpleName();
    private final SearchServiceSigned mSearchServiceSigned;
    private final BrowseServiceSigned mBrowseServiceSigned;
    private final YouTubeSignInManager mSignInManager;
    private static YouTubeMediaGroupManagerSigned sInstance;

    private YouTubeMediaGroupManagerSigned() {
        mSearchServiceSigned = SearchServiceSigned.instance();
        mBrowseServiceSigned = BrowseServiceSigned.instance();
        mSignInManager = YouTubeSignInManager.instance();
    }

    public static YouTubeMediaGroupManagerSigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupManagerSigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
        BrowseServiceSigned.unhold();
        SearchServiceUnsigned.unhold();
    }

    @Override
    public MediaGroup getSearch(String searchText) {
        SearchResult searchResult = mSearchServiceSigned.getSearch(searchText, mSignInManager.getAuthorizationHeader());
        return YouTubeMediaGroup.from(searchResult, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public MediaGroup getSubscriptions() {
        return YouTubeMediaGroup.from(mBrowseServiceSigned.getSubscriptions(mSignInManager.getAuthorizationHeader()), MediaGroup.TYPE_SUBSCRIPTIONS);
    }

    @Override
    public MediaGroup getRecommended() {
        return YouTubeMediaGroup.from(mBrowseServiceSigned.getRecommended(mSignInManager.getAuthorizationHeader()));
    }

    @Override
    public MediaGroup getHistory() {
        return YouTubeMediaGroup.from(mBrowseServiceSigned.getHistory(mSignInManager.getAuthorizationHeader()), MediaGroup.TYPE_HISTORY);
    }

    @Override
    public List<MediaGroup> getFirstHomeGroups() {
        Log.d(TAG, "Emitting first home groups...");
        List<BrowseSection> browseTabs = mBrowseServiceSigned.getHomeSections(mSignInManager.getAuthorizationHeader());
        return YouTubeMediaGroup.from(browseTabs);
    }

    @Override
    public List<MediaGroup> getNextHomeGroups() {
        Log.d(TAG, "Emitting next home groups...");
        List<BrowseSection> browseTabs = mBrowseServiceSigned.getNextHomeSections(mSignInManager.getAuthorizationHeader());
        return YouTubeMediaGroup.from(browseTabs);
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        Log.d(TAG, "Continue group " + mediaGroup.getTitle() + "...");

        if (mediaGroup.getType() == MediaGroup.TYPE_SEARCH) {
            return YouTubeMediaGroup.from(
                    mSearchServiceSigned.continueSearch(YouTubeMediaServiceHelper.extractNextKey(mediaGroup), mSignInManager.getAuthorizationHeader()),
                    mediaGroup);
        }

        return YouTubeMediaGroup.from(
                mBrowseServiceSigned.continueSection(YouTubeMediaServiceHelper.extractNextKey(mediaGroup), mSignInManager.getAuthorizationHeader()),
                mediaGroup
        );
    }
}