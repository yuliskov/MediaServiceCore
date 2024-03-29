package com.liskovsoft.driveapi.app;

import com.liskovsoft.driveapi.oauth2.OAuth2Api;

public class AppService {
    private static final String TAG = AppService.class.getSimpleName();
    private static AppService sInstance;

    private AppService() {
        
    }

    public static AppService instance() {
        if (sInstance == null) {
            sInstance = new AppService();
        }

        return sInstance;
    }

    /**
     * Constant used in {@link OAuth2Api}
     */
    public String getClientId() {
        return null;
    }

    /**
     * Constant used in {@link OAuth2Api}
     */
    public String getClientSecret() {
        return null;
    }

    public String getVisitorId() {
        return null;
    }
}
