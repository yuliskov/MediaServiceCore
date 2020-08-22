package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.search.SearchServiceSigned;
import com.liskovsoft.youtubeapi.search.SearchServiceUnsigned;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaGroupManagerSigned implements MediaGroupManager {
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
        SearchResult searchResult = mSearchServiceSigned.getSearch(searchText, mSignInManager.getAuthorization());
        return YouTubeMediaGroup.from(searchResult, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaGroup> getSearchObserve(String searchText) {
        return Observable.fromCallable(() -> getSearch(searchText));
    }

    @Override
    public MediaGroup getSubscriptions() {
        return YouTubeMediaGroup.from(mBrowseServiceSigned.getSubscriptions(mSignInManager.getAuthorization()), MediaGroup.TYPE_SUBSCRIPTIONS);
    }

    @Override
    public MediaGroup getRecommended() {
        return YouTubeMediaGroup.from(mBrowseServiceSigned.getRecommended(mSignInManager.getAuthorization()));
    }

    @Override
    public Observable<MediaGroup> getRecommendedObserve() {
        return Observable.fromCallable(this::getRecommended);
    }

    @Override
    public MediaGroup getHistory() {
        return YouTubeMediaGroup.from(mBrowseServiceSigned.getHistory(mSignInManager.getAuthorization()), MediaGroup.TYPE_HISTORY);
    }

    @Override
    public Observable<MediaGroup> getHistoryObserve() {
        return Observable.fromCallable(this::getHistory);
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsObserve() {
        return Observable.fromCallable(this::getSubscriptions);
    }

    @Override
    public List<MediaGroup> getHome() {
        List<MediaGroup> result = new ArrayList<>();

        List<MediaGroup> groups = getFirstHomeGroups();

        while (!groups.isEmpty()) {
            result.addAll(groups);
            groups = getNextHomeGroups();
        }

        return result;
    }

    @Override
    public Observable<List<MediaGroup>> getHomeObserve() {
        return Observable.create(emitter -> {
            List<MediaGroup> groups = getFirstHomeGroups();

            while (!groups.isEmpty()) {
                emitter.onNext(groups);
                groups = getNextHomeGroups();
            }

            emitter.onComplete();
        });
    }

    private List<MediaGroup> getFirstHomeGroups() {
        Log.d(TAG, "Emitting first home groups...");
        List<BrowseSection> browseTabs = mBrowseServiceSigned.getHomeSections(mSignInManager.getAuthorization());
        return YouTubeMediaGroup.from(browseTabs);
    }

    private List<MediaGroup> getNextHomeGroups() {
        Log.d(TAG, "Emitting next home groups...");
        List<BrowseSection> browseTabs = mBrowseServiceSigned.getNextHomeSections(mSignInManager.getAuthorization());
        return YouTubeMediaGroup.from(browseTabs);
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        Log.d(TAG, "Continue group " + mediaGroup.getTitle() + "...");

        if (mediaGroup.getType() == MediaGroup.TYPE_SEARCH) {
            return YouTubeMediaGroup.from(
                    mSearchServiceSigned.continueSearch(YouTubeMediaServiceHelper.extractNextKey(mediaGroup), mSignInManager.getAuthorization()),
                    mediaGroup);
        }

        return YouTubeMediaGroup.from(
                mBrowseServiceSigned.continueSection(YouTubeMediaServiceHelper.extractNextKey(mediaGroup), mSignInManager.getAuthorization()),
                mediaGroup
        );
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup) {
        return Observable.fromCallable(() -> continueGroup(mediaGroup));
    }
}
