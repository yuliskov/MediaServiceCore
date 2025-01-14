package com.liskovsoft.youtubeapi.common.js;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8ResultUndefined;
import com.eclipsesource.v8.V8ScriptExecutionException;
import com.liskovsoft.sharedutils.mylogger.Log;

import java.util.List;

public final class V8Runtime {
    private static final String TAG = V8Runtime.class.getSimpleName();
    private static V8Runtime sInstance;
    private V8 mRuntime;

    //static {
    //    // Fix? J2V8 native library not loaded (j2v8-android-arm_32/j2v8-android-arm_32)
    //    System.loadLibrary("j2v8");
    //}

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

    public String evaluate(final String source) {
        return evaluateSafe(source);
    }

    public String evaluate(final List<String> sources) {
        return evaluateSafe(sources);
    }

    /**
     * Not a thread safe. Possible 'Invalid V8 thread access' errors.
     */
    private String evaluateUnsafe(final String source) {
        String result = null;

        try {
            if (mRuntime == null) {
                mRuntime = V8.createV8Runtime();
            }
            mRuntime.getLocker().acquire(); // Possible 'Invalid V8 thread access' errors
            result = mRuntime.executeStringScript(source);
        } catch (V8ScriptExecutionException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            if (mRuntime != null) {
                mRuntime.getLocker().release(); // Possible 'Invalid V8 thread access' errors
            }
        }

        return result;
    }

    /**
     * Thread safe solution but performance a bit slow.
     */
    private String evaluateSafe(final String source) {
        V8 runtime = null;
        String result = null;

        try {
            runtime = V8.createV8Runtime();
            result = runtime.executeStringScript(source);
        } catch (V8ScriptExecutionException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            if (runtime != null) {
                runtime.release(false);
            }
        }

        return result;
    }

    /**
     * Thread safe solution but performance a bit slow.
     */
    private String evaluateSafe(final List<String> sources) {
        V8 runtime = null;
        String result = null;

        try {
            runtime = V8.createV8Runtime();
            for (String source : sources) {
                try {
                    result = runtime.executeStringScript(source);
                } catch (V8ResultUndefined e) {
                    // NOP
                }
            }
        } catch (V8ScriptExecutionException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            if (runtime != null) {
                runtime.release(false);
            }
        }

        return result;
    }
}
