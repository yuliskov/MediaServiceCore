package com.liskovsoft.youtubeapi.pageinfo.V1;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.pageinfo.PageInfoServiceBase;
import com.liskovsoft.youtubeapi.pageinfo.models.PageInfo;

import retrofit2.Call;

public class PageInfoServiceUnsigned extends PageInfoServiceBase {
    private static PageInfoServiceUnsigned sInstance;
    private final PageInfoManagerUnsigned mPageInfoManagerUnsigned;
    private final LocaleManager mLocaleManager;

    private PageInfoServiceUnsigned() {
        mPageInfoManagerUnsigned = RetrofitHelper.withRegExpAndJsonPath(PageInfoManagerUnsigned.class);
        mLocaleManager = LocaleManager.instance();
    }

    public static PageInfoServiceUnsigned instance() {
        if (sInstance == null) {
            sInstance = new PageInfoServiceUnsigned();
        }

        return sInstance;
    }

    public PageInfo getPageInfo(String videoId) {
        return getPageInfoHls(videoId);
    }

    private PageInfo getPageInfoHls(String videoId) {
        Call<PageInfo> wrapper = mPageInfoManagerUnsigned.getPageInfoHls(videoId, mLocaleManager.getLanguage());

        return RetrofitHelper.get(wrapper);
    }

    private PageInfo getPageInfoRestricted(String videoId) {
        Call<PageInfo> wrapper = mPageInfoManagerUnsigned.getPageInfoRestricted(videoId, mLocaleManager.getLanguage());

        return RetrofitHelper.get(wrapper);
    }
}
