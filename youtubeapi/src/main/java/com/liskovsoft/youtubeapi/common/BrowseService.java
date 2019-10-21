package com.liskovsoft.youtubeapi.common;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.BrowserAuth;
import com.liskovsoft.youtubeapi.auth.models.AccessToken;
import com.liskovsoft.youtubeapi.browse.BrowseManager;
import com.liskovsoft.youtubeapi.browse.BrowseParams;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import okhttp3.RequestBody;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps result from the {@link BrowserAuth} and {@link BrowseManager}
 */
public class BrowseService {
    private static final String TAG = BrowseService.class.getSimpleName();
    private BrowseManager mBrowseManager;
    private String mNextPageKey;
    private String mAuthorization;

    public BrowseService() {
        initToken();
    }

    public List<VideoItem> getSubscriptions() {
        return getAuthSection(BrowseParams.getSubscriptionsQuery());
    }

    /**
     * Method uses results from the {@link #getSubscriptions()} call
     * @return video items
     */
    public List<VideoItem> getNextSubscriptions() {
        return getNextAuthSection();
    }

    public List<VideoItem> getRecommended() {
        return getTabbedAuthSection(BrowseParams.getHomeQuery());
    }

    public List<VideoItem> getNextRecommended() {
        return getNextAuthSection();
    }

    public List<VideoItem> getHistory() {
        return getAuthSection(BrowseParams.getHistoryQuery());
    }

    public List<VideoItem> getNextHistory() {
        return getNextAuthSection();
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

        if (token != null) {
            mAuthorization = String.format("%s %s", token.getTokenType(), token.getAccessToken());
        }
    }

    private List<VideoItem> getAuthSection(String query) {
        if (mAuthorization == null) {
            Log.e(TAG, "getAuthSection: authorization is null.");
            return new ArrayList<>();
        }

        BrowseManager manager = getBrowseManager();

        Call<BrowseResult> wrapper = manager.getBrowseResult(query, mAuthorization);

        BrowseResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getAuthSection: browse result is null");
            return new ArrayList<>();
        }

        mNextPageKey = browseResult.getNextPageKey();

        return browseResult.getVideoItems();
    }

    private List<VideoItem> getNextAuthSection() {
        if (mAuthorization == null) {
            Log.e(TAG, "getNextAuthSection: authorization is null.");
            return new ArrayList<>();
        }

        if (mNextPageKey == null) {
            Log.e(TAG, "getNextAuthSection: next search key is null.");
            return new ArrayList<>();
        }

        BrowseManager manager = getBrowseManager();
        Call<NextBrowseResult> wrapper = manager.getNextBrowseResult(BrowseParams.getNextBrowseQuery(mNextPageKey), mAuthorization);
        NextBrowseResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getNextAuthSection: browseResult is null. Maybe invalid next key: " + mNextPageKey);
            return new ArrayList<>();
        }

        mNextPageKey = browseResult.getNextPageKey();

        return browseResult.getVideoItems();
    }

    private List<VideoItem> getTabbedAuthSection(String query) {
        if (mAuthorization == null) {
            Log.e(TAG, "getTabbedAuthSection: authorization is null.");
            return new ArrayList<>();
        }

        BrowseManager manager = getBrowseManager();

        Call<TabbedBrowseResult> wrapper = manager.getTabbedBrowseResult(query, mAuthorization);

        TabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);


        if (browseResult == null) {
            Log.e(TAG, "getTabbedAuthSection: browseResult is null");
            return new ArrayList<>();
        }

        List<BrowseTab> browseTabs = browseResult.getBrowseTabs();

        BrowseSection browseSection = getFirstTabbedSection(browseTabs);

        if (browseSection != null) {
            mNextPageKey = browseSection.getNextPageKey();
            return browseSection.getVideoItems();
        }

        return new ArrayList<>();
    }

    // TODO: maybe choose other section
    private BrowseSection getFirstTabbedSection(List<BrowseTab> browseTabs) {
        if (browseTabs != null) {
            BrowseTab browseTab = browseTabs.get(0);

            if (browseTab != null) {
                List<BrowseSection> sections = browseTab.getSections();

                if (sections != null) {
                    return sections.get(0);
                }
            }
        }

        return null;
    }
}
