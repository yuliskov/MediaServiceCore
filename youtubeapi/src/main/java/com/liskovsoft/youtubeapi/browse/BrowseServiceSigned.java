package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.NextTabbedBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * For auth users only!<br/>
 * Wraps result from the {@link AuthManager} and {@link BrowseManagerSigned}
 */
public class BrowseServiceSigned {
    private static final String TAG = BrowseServiceSigned.class.getSimpleName();
    private final BrowseManagerSigned mBrowseManagerSigned;
    private static BrowseServiceSigned sInstance;
    private String mNextHomeTabsKey;

    private BrowseServiceSigned() {
        mBrowseManagerSigned = RetrofitHelper.withJsonPath(BrowseManagerSigned.class);
    }

    public static BrowseServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new BrowseServiceSigned();
        }

        return sInstance;
    }

    public BrowseResult getSubscriptions(String authorization) {
        return getSection(BrowseManagerParams.getSubscriptionsQuery(), authorization);
    }

    public BrowseSection getRecommended(String authorization) {
        return getTabbedSection(BrowseManagerParams.getHomeQuery(), authorization);
    }

    public BrowseResult getHistory(String authorization) {
        return getSection(BrowseManagerParams.getHistoryQuery(), authorization);
    }

    private BrowseResult getSection(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getAuthSection: authorization is null.");
            return null;
        }

        Call<BrowseResult> wrapper = mBrowseManagerSigned.getBrowseResult(query, authorization);

        BrowseResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getAuthSection: browse result is null");
        }

        return browseResult;
    }

    private NextBrowseResult getNextSection(String nextPageKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getNextAuthSection: authorization is null.");
            return null;
        }

        if (nextPageKey == null) {
            Log.e(TAG, "getNextAuthSection: next search key is null.");
            return null;
        }

        Call<NextBrowseResult> wrapper = mBrowseManagerSigned.getNextBrowseResult(BrowseManagerParams.getNextBrowseQuery(nextPageKey), authorization);
        NextBrowseResult browseResult = RetrofitHelper.get(wrapper);

        if (browseResult == null) {
            Log.e(TAG, "getNextAuthSection: browseResult is null. Maybe invalid next key: " + nextPageKey);
        }

        return browseResult;
    }

    private BrowseSection getTabbedSection(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getTabbedAuthSection: authorization is null.");
            return null;
        }

        Call<TabbedBrowseResult> wrapper = mBrowseManagerSigned.getTabbedBrowseResult(query, authorization);

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
        return getNextSection(nextKey, authorization);
    }

    public List<BrowseSection> getHomeSections(String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getHomeSections: authorization is null.");
            return null;
        }

        TabbedBrowseResult homeTabs = getTabbedResult(BrowseManagerParams.getHomeQuery(), authorization);

        if (homeTabs == null) {
            Log.e(TAG, "Home tabs are empty");
            return new ArrayList<>();
        }
        
        mNextHomeTabsKey = findHomeTab(homeTabs).getNextPageKey();

        return findHomeTab(homeTabs).getSections();
    }

    public List<BrowseSection> getNextHomeSections(String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getNextHomeSections: authorization is null.");
            return null;
        }

        NextTabbedBrowseResult nextHomeTabs = null;

        if (mNextHomeTabsKey != null) {
            nextHomeTabs = getNextTabbedResult(mNextHomeTabsKey, authorization);

            if (nextHomeTabs != null) {
                mNextHomeTabsKey = nextHomeTabs.getNextPageKey();
            } else {
                mNextHomeTabsKey = null;
            }
        }

        if (nextHomeTabs == null) {
            Log.e(TAG, "NextHomeTabs are empty");
            return new ArrayList<>();
        }

        return nextHomeTabs.getSections();
    }

    private TabbedBrowseResult getTabbedResult(String query, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getTabbedResult: authorization is null.");
            return null;
        }

        Call<TabbedBrowseResult> wrapper = mBrowseManagerSigned.getTabbedBrowseResult(query, authorization);

        TabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private NextTabbedBrowseResult getNextTabbedResult(String nextKey, String authorization) {
        if (authorization == null) {
            Log.e(TAG, "getNextTabbedResult: authorization is null.");
            return null;
        }

        String query = BrowseManagerParams.getNextBrowseQuery(nextKey);

        Call<NextTabbedBrowseResult> wrapper = mBrowseManagerSigned.getNextTabbedBrowseResult(query, authorization);

        NextTabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);

        return browseResult;
    }

    private BrowseTab findHomeTab(TabbedBrowseResult homeTabs) {
        return homeTabs.getBrowseTabs().get(0);
    }
}
