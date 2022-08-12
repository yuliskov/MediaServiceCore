package com.liskovsoft.youtubeapi.common.js;

import com.eclipsesource.v8.V8;

public final class V8Runtime {
    private static V8Runtime sInstance;
    private final V8 mRuntime;

    private V8Runtime() {
        mRuntime = V8.createV8Runtime();
    }

    public static V8Runtime instance() {
        if (sInstance == null) {
            sInstance = new V8Runtime();
        }

        return sInstance;
    }

    public static void unhold() {
        if (sInstance != null) {
            sInstance.mRuntime.release();
        }

        sInstance = null;
    }

    public String evaluate(final String source) {
        mRuntime.getLocker().acquire();
        String result = mRuntime.executeStringScript(source);
        mRuntime.getLocker().release();
        return result;
    }
}
