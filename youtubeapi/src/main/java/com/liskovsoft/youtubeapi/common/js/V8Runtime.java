package com.liskovsoft.youtubeapi.common.js;

import com.eclipsesource.v8.V8;

public final class V8Runtime {
    private static V8Runtime sInstance;
    private V8 mRuntime;

    private V8Runtime() {
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

    /**
     * Not a thread safe. Possible 'Invalid V8 thread access' errors.
     */
    public String evaluateUnsafe(final String source) {
        if (mRuntime == null) {
            mRuntime = V8.createV8Runtime();
        }

        String result;

        try {
            mRuntime.getLocker().acquire(); // Possible 'Invalid V8 thread access' errors
            result = mRuntime.executeStringScript(source);
        } finally {
            mRuntime.getLocker().release(); // Possible 'Invalid V8 thread access' errors
        }

        return result;
    }

    public String evaluate(final String source) {
        V8 runtime = null;

        try {
            runtime = V8.createV8Runtime();
            return runtime.executeStringScript(source);
        } finally {
            if (runtime != null) {
                runtime.release(false);
            }
        }
    }
}
