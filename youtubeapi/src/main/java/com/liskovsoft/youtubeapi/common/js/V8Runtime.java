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
        // NOTE: using 'release' produces 'Invalid V8 thread access: the locker has been released!'
        //if (sInstance != null) {
        //    sInstance.mRuntime.release();
        //}

        sInstance = null;
    }

    public String evaluate(final String source) {
        String result;

        try {
            mRuntime.getLocker().acquire();
            result = mRuntime.executeStringScript(source);
        } finally { // Possible fix for acquire(): "Invalid V8 thread access: current thread is..."
            mRuntime.getLocker().release();
        }

        return result;
    }
}
