package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.SignInManager;

public class YouTubeSignInManager implements SignInManager {
    private static YouTubeSignInManager sInstance;
    private boolean mIsSigned;
    private String mRefreshToken;

    private YouTubeSignInManager() {
        // mRefreshToken = prefs.getRefreshToken();

        if (mRefreshToken != null) {
            mIsSigned = true;
        }
    }

    public static YouTubeSignInManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeSignInManager();
        }

        return sInstance;
    }

    @Override
    public String getUserCode() {
        return null;
    }

    @Override
    public void applyResult() {

    }

    public boolean isSigned() {
        return mIsSigned;
    }

    public String getAuthorization() {
        // get or create authorization on fly
        return null;
    }
}
