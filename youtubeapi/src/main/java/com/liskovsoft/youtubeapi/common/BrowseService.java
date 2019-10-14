package com.liskovsoft.youtubeapi.common;

import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.BrowserAuth;
import com.liskovsoft.youtubeapi.auth.models.AccessToken;
import com.liskovsoft.youtubeapi.browse.BrowseManager;
import com.liskovsoft.youtubeapi.browse.BrowseParams;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import okhttp3.RequestBody;
import retrofit2.Call;

import java.util.List;

/**
 * Wraps result from the {@link BrowserAuth} and {@link BrowseManager}
 */
public class BrowseService {
    private BrowseManager mBrowseManager;
    private String mNextPageKey;
    private String mAuthorization;

    public BrowseService() {
        initToken();
    }

    public List<VideoItem> getSubscriptions() {
        if (mAuthorization == null) {
            return null;
        }

        BrowseManager manager = getBrowseManager();

        Call<BrowseResult> wrapper = manager.getBrowseResult(BrowseParams.getSubscriptionsQuery(), mAuthorization);

        BrowseResult browseResult = RetrofitHelper.get(wrapper);


        if (browseResult == null) {
            throw new IllegalStateException("Invalid browse result");
        }

        mNextPageKey = browseResult.getNextPageKey();

        return browseResult.getVideoItems();
    }

    /**
     * Method uses results from the {@link #getSubscriptions()} call
     * @return video items
     */
    public List<VideoItem> getNextSubscriptions() {
        if (mNextPageKey == null) {
            throw new IllegalStateException("Can't get next search page. Next search key is empty.");
        }

        BrowseManager manager = getBrowseManager();
        Call<NextBrowseResult> wrapper = manager.getNextBrowseResult(BrowseParams.getNextBrowseQuery(mNextPageKey), mAuthorization);
        NextBrowseResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            throw new IllegalStateException("Invalid next page search result for key " + mNextPageKey);
        }

        mNextPageKey = browseResult.getNextPageKey();

        return browseResult.getVideoItems();
    }

    private BrowseManager getBrowseManager() {
        if (mBrowseManager == null) {
            mBrowseManager = RetrofitHelper.withJsonPath(BrowseManager.class);
        }

        return mBrowseManager;
    }

    private void initToken() {
        if (GlobalPreferences.sInstance == null) {
            return;
        }

        String rawAuthData = GlobalPreferences.sInstance.getRawAuthData();

        if (rawAuthData == null) {
            return;
        }

        BrowserAuth authService = RetrofitHelper.withGson(BrowserAuth.class);
        Call<AccessToken> wrapper = authService.getAuthToken(RequestBody.create(null, rawAuthData.getBytes()));
        AccessToken token = RetrofitHelper.get(wrapper);
        mAuthorization = String.format("%s %s", token.getTokenType(), token.getAccessToken());
    }
}
