package com.liskovsoft.youtubeapi.pageinfo.V1;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.pageinfo.PageInfoServiceBase;
import com.liskovsoft.youtubeapi.pageinfo.models.PageInfo;
import com.liskovsoft.youtubeapi.videoinfo.V1.VideoInfoServiceSigned;

import retrofit2.Call;

public class PageInfoServiceSigned extends PageInfoServiceBase {
    private static PageInfoServiceSigned sInstance;
    private final PageInfoManagerSigned mPageInfoManagerSigned;
    private final LocaleManager mLocaleManager;

    private PageInfoServiceSigned() {
        mPageInfoManagerSigned = RetrofitHelper.withRegExpAndJsonPath(PageInfoManagerSigned.class);
        mLocaleManager = LocaleManager.instance();
    }

    public static PageInfoServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new PageInfoServiceSigned();
        }

        return sInstance;
    }

    public PageInfo getPageInfo(String videoId, String authorization) {
        return getPageInfoHls(videoId, authorization);
    }

    private PageInfo getPageInfoHls(String videoId, String authorization) {
        Call<PageInfo> wrapper = mPageInfoManagerSigned.getPageInfoHls(videoId, ServiceHelper.getToken(authorization), mLocaleManager.getLanguage());

        return RetrofitHelper.get(wrapper);
    }

    private PageInfo getPageInfoRegular(String videoId, String authorization) {
        Call<PageInfo> wrapper = mPageInfoManagerSigned.getPageInfoRegular(videoId, ServiceHelper.getToken(authorization), mLocaleManager.getLanguage());

        return RetrofitHelper.get(wrapper);
    }

    private PageInfo getPageInfoRestricted(String videoId, String authorization) {
        Call<PageInfo> wrapper = mPageInfoManagerSigned.getPageInfoRestricted(videoId, ServiceHelper.getToken(authorization), mLocaleManager.getLanguage());

        return RetrofitHelper.get(wrapper);
    }
}
