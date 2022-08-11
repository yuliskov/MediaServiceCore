package com.liskovsoft.youtubeapi.pageinfo;

import com.liskovsoft.youtubeapi.app.AppService;

public abstract class PageInfoServiceBase {
    protected final AppService mAppService;

    protected PageInfoServiceBase() {
        mAppService = AppService.instance();
    }
}
