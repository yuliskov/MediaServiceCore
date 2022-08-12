package com.liskovsoft.youtubeapi.common.js;

import com.eclipsesource.v8.V8;

public final class V8Runtime {
    private V8Runtime() {
    }

    public static String evaluate(final String source) {
        V8 runtime = null;

        try {
            runtime = V8.createV8Runtime();
            return runtime.executeStringScript(source);
        } finally {
            if (runtime != null) {
                runtime.release();
            }
        }
    }
}
