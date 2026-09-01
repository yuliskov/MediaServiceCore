package com.liskovsoft.googlecommon.common.js;

import android.webkit.CookieManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.javascriptengine.IsolateStartupParameters;
import androidx.javascriptengine.JavaScriptIsolate;
import androidx.javascriptengine.JavaScriptSandbox;

import com.liskovsoft.youtubeapi.app.AppService;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Process-wide owner of the System WebView-backed AndroidX JavaScriptEngine.
 * Each independent caller can create an isolate through {@link Session}.
 */
public final class JavaScriptRuntime {
    private static final long CONNECT_TIMEOUT_SECONDS = 20;
    private static final long EVALUATION_TIMEOUT_SECONDS = 60;
    private static final int MAX_HEAP_SIZE_BYTES = 128 * 1024 * 1024;
    private static final Object SANDBOX_LOCK = new Object();

    private static JavaScriptSandbox sSandbox;

    private JavaScriptRuntime() {
    }

    public static boolean isSupported() {
        try {
            CookieManager.getInstance();
            return JavaScriptSandbox.isSupported();
        } catch (Throwable ignored) {
            return false;
        }
    }

    @NonNull
    public static Session createSession() throws JavaScriptRuntimeException {
        synchronized (SANDBOX_LOCK) {
            JavaScriptSandbox sandbox = getOrCreateSandbox();
            JavaScriptIsolate isolate;

            if (sandbox.isFeatureSupported(JavaScriptSandbox.JS_FEATURE_ISOLATE_MAX_HEAP_SIZE)) {
                IsolateStartupParameters parameters = new IsolateStartupParameters();
                parameters.setMaxHeapSizeBytes(MAX_HEAP_SIZE_BYTES);
                isolate = sandbox.createIsolate(parameters);
            } else {
                isolate = sandbox.createIsolate();
            }

            return new Session(isolate);
        }
    }

    @NonNull
    public static String evaluate(@NonNull String source) throws JavaScriptRuntimeException {
        try (Session session = createSession()) {
            return session.evaluate(source);
        }
    }

    @Nullable
    public static String evaluate(@NonNull List<String> sources) throws JavaScriptRuntimeException {
        try (Session session = createSession()) {
            String result = null;
            for (String source : sources) {
                result = session.evaluate(source);
            }
            return result;
        }
    }

    public static void close() {
        synchronized (SANDBOX_LOCK) {
            if (sSandbox != null) {
                try {
                    sSandbox.close();
                } finally {
                    sSandbox = null;
                }
            }
        }
    }

    @NonNull
    private static JavaScriptSandbox getOrCreateSandbox() throws JavaScriptRuntimeException {
        if (sSandbox != null) {
            return sSandbox;
        }
        if (!isSupported()) {
            throw new JavaScriptRuntimeException(
                    "AndroidX JavaScriptEngine is not supported by this device/System WebView");
        }

        try {
            sSandbox = JavaScriptSandbox.createConnectedInstanceAsync(
                            AppService.instance().getContext().getApplicationContext())
                    .get(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return sSandbox;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new JavaScriptRuntimeException("Interrupted while connecting to JavaScriptEngine", e);
        } catch (ExecutionException | TimeoutException e) {
            throw new JavaScriptRuntimeException("Could not connect to JavaScriptEngine", unwrap(e));
        }
    }

    @NonNull
    private static Throwable unwrap(@NonNull Exception error) {
        Throwable cause = error instanceof ExecutionException ? error.getCause() : error;
        return cause != null ? cause : error;
    }

    public static final class Session implements AutoCloseable {
        private JavaScriptIsolate mIsolate;

        private Session(@NonNull JavaScriptIsolate isolate) {
            mIsolate = isolate;
        }

        @NonNull
        public synchronized String evaluate(@NonNull String source)
                throws JavaScriptRuntimeException {
            if (mIsolate == null) {
                throw new JavaScriptRuntimeException("JavaScript isolate is closed");
            }

            try {
                return mIsolate.evaluateJavaScriptAsync(source)
                        .get(EVALUATION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new JavaScriptRuntimeException("JavaScript evaluation was interrupted", e);
            } catch (ExecutionException | TimeoutException e) {
                throw new JavaScriptRuntimeException("JavaScript evaluation failed", unwrap(e));
            }
        }

        @Override
        public synchronized void close() {
            if (mIsolate != null) {
                mIsolate.close();
                mIsolate = null;
            }
        }
    }

    public static final class JavaScriptRuntimeException extends Exception {
        public JavaScriptRuntimeException(@NonNull String message) {
            super(message);
        }

        public JavaScriptRuntimeException(@NonNull String message, @NonNull Throwable cause) {
            super(message, cause);
        }
    }
}
