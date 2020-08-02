package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.auth.AuthService;
import com.liskovsoft.youtubeapi.auth.models.RefreshTokenResult;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import retrofit2.Call;

import java.util.List;

/**
 * For auth users only!<br/>
 * Wraps result from the {@link AuthManager} and {@link BrowseManagerSigned}
 */
public class BrowseServiceSigned {
    private static final String TAG = BrowseServiceSigned.class.getSimpleName();
    private BrowseManagerSigned mBrowseManager;

    public BrowseServiceSigned() {
        //initToken();
    }

    public BrowseResult getSubscriptions(String authorization) {
        return getAuthSection(BrowseParams.getSubscriptionsQuery(), authorization);
    }

    public BrowseSection getRecommended(String authorization) {
        return getTabbedAuthSection(BrowseParams.getHomeQuery(), authorization);
    }

    public BrowseResult getHistory(String authorization) {
        return getAuthSection(BrowseParams.getHistoryQuery(), authorization);
    }

    private BrowseManagerSigned getBrowseManager() {
        if (mBrowseManager == null) {
            mBrowseManager = RetrofitHelper.withJsonPath(BrowseManagerSigned.class);
        }

        return mBrowseManager;
    }

    private BrowseResult getAuthSection(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getAuthSection: authorization is null.");
            return null;
        }

        BrowseManagerSigned manager = getBrowseManager();

        Call<BrowseResult> wrapper = manager.getBrowseResult(query, authorization);

        BrowseResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getAuthSection: browse result is null");
        }

        return browseResult;
    }

    private NextBrowseResult getNextAuthSection(String nextPageKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getNextAuthSection: authorization is null.");
            return null;
        }

        if (nextPageKey == null) {
            Log.e(TAG, "getNextAuthSection: next search key is null.");
            return null;
        }

        BrowseManagerSigned manager = getBrowseManager();
        Call<NextBrowseResult> wrapper = manager.getNextBrowseResult(BrowseParams.getNextBrowseQuery(nextPageKey), authorization);
        NextBrowseResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getNextAuthSection: browseResult is null. Maybe invalid next key: " + nextPageKey);
        }

        return browseResult;
    }

    private BrowseSection getTabbedAuthSection(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getTabbedAuthSection: authorization is null.");
            return null;
        }

        BrowseManagerSigned manager = getBrowseManager();

        Call<TabbedBrowseResult> wrapper = manager.getTabbedBrowseResult(query, authorization);

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

    public NextBrowseResult continueSection(String nextKey, String authorization) {
        return getNextAuthSection(nextKey, authorization);
    }
}
