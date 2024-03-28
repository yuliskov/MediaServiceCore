package com.liskovsoft.driveapi.app;

import com.liskovsoft.driveapi.auth.V2.AuthApi;

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
     * Constant used in {@link AuthApi}
     */
    public String getClientId() {
        return null;
    }

    /**
     * Constant used in {@link AuthApi}
     */
    public String getClientSecret() {
        return null;
    }

    public String getVisitorId() {
        return null;
    }
}
