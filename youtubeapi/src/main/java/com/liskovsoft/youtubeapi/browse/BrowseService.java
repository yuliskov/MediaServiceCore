package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class BrowseService {
    private static final String TAG = BrowseService.class.getSimpleName();
    private BrowseManager mBrowseManager;
    private String mNextPageKey;

    public List<BrowseTab> getHomeTabs() {
        return getTabbedResult(BrowseParams.getHomeQuery());
    }

    private BrowseManager getBrowseManager() {
        if (mBrowseManager == null) {
            mBrowseManager = RetrofitHelper.withJsonPath(BrowseManager.class);
        }

        return mBrowseManager;
    }

    private List<BrowseTab> getTabbedResult(String query) {
        BrowseManager manager = getBrowseManager();

        Call<TabbedBrowseResult> wrapper = manager.getTabbedBrowseResult(query);

        TabbedBrowseResult browseResult = RetrofitHelper.get(wrapper);


        if (browseResult == null) {
            Log.e(TAG, "getTabbedSections: browseResult is null");
            return new ArrayList<>();
        }

        List<BrowseTab> browseTabs = browseResult.getBrowseTabs();

        return browseTabs;
    }
}
