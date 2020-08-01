package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.auth.models.AccessTokenResult;
import com.liskovsoft.youtubeapi.auth.models.RefreshTokenResult;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import okhttp3.RequestBody;
import retrofit2.Call;

import java.util.List;

/**
 * For auth users only!<br/>
 * Wraps result from the {@link AuthManager} and {@link BrowseManagerSigned}
 */
public class BrowseServiceSigned {
    private static final String TAG = BrowseServiceSigned.class.getSimpleName();
    private BrowseManagerSigned mBrowseManager;
    private String mAuthorization;

    public BrowseServiceSigned() {
        initToken();
    }

    public BrowseResult getSubscriptions() {
        return getAuthSection(BrowseParams.getSubscriptionsQuery());
    }

    public BrowseSection getRecommended() {
        return getTabbedAuthSection(BrowseParams.getHomeQuery());
    }

    public BrowseResult getHistory() {
        return getAuthSection(BrowseParams.getHistoryQuery());
    }

    private BrowseManagerSigned getBrowseManager() {
        if (mBrowseManager == null) {
            mBrowseManager = RetrofitHelper.withJsonPath(BrowseManagerSigned.class);
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

        AuthManager authService = RetrofitHelper.withGson(AuthManager.class);
        Call<RefreshTokenResult> wrapper = authService.getRefreshToken(RequestBody.create(null, rawAuthData.getBytes()));
        RefreshTokenResult token = RetrofitHelper.get(wrapper);

        if (token != null) {
            mAuthorization = String.format("%s %s", token.getTokenType(), token.getAccessToken());
        }
    }

    private BrowseResult getAuthSection(String query) {
        if (mAuthorization == null) {
            Log.e(TAG, "getAuthSection: authorization is null.");
            return null;
        }

        BrowseManagerSigned manager = getBrowseManager();

        Call<BrowseResult> wrapper = manager.getBrowseResult(query, mAuthorization);

        BrowseResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getAuthSection: browse result is null");
        }

        return browseResult;
    }

    private NextBrowseResult getNextAuthSection(String nextPageKey) {
        if (mAuthorization == null) {
            Log.e(TAG, "getNextAuthSection: authorization is null.");
            return null;
        }

        if (nextPageKey == null) {
            Log.e(TAG, "getNextAuthSection: next search key is null.");
            return null;
        }

        BrowseManagerSigned manager = getBrowseManager();
        Call<NextBrowseResult> wrapper = manager.getNextBrowseResult(BrowseParams.getNextBrowseQuery(nextPageKey), mAuthorization);
        NextBrowseResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getNextAuthSection: browseResult is null. Maybe invalid next key: " + nextPageKey);
        }

        return browseResult;
    }

    private BrowseSection getTabbedAuthSection(String query) {
        if (mAuthorization == null) {
            Log.e(TAG, "getTabbedAuthSection: authorization is null.");
            return null;
        }

        BrowseManagerSigned manager = getBrowseManager();

        Call<TabbedBrowseResult> wrapper = manager.getTabbedBrowseResult(query, mAuthorization);

        TabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);


        if (browseResult == null) {
            Log.e(TAG, "getTabbedAuthSection: browseResult is null");
            return null;
        }

        List<BrowseTab> browseTabs = browseResult.getBrowseTabs();

        BrowseSection browseSection = getFirstTabbedSection(browseTabs);

        return browseSection;
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

    public NextBrowseResult continueSection(String nextKey) {
        return getNextAuthSection(nextKey);
    }
}
