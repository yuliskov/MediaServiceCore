package com.liskovsoft.youtubeapi.common.helpers;

import com.liskovsoft.sharedutils.mylogger.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.OnErrorNotImplementedException;

import java.util.concurrent.Callable;

public class ObservableHelper {
    private static final String TAG = ObservableHelper.class.getSimpleName();

    public static Observable<Void> fromVoidable(Runnable callback) {
        return Observable.create(emitter -> {
            callback.run();
            emitter.onComplete();
        });
    }

    public static <T> Observable<T> fromNullable(Callable<T> callback) {
        return Observable.create(emitter -> {
            T result = callback.call();

            if (result != null) {
                emitter.onNext(result);
            } else {
                // Be aware of OnErrorNotImplementedException exception if error handler not implemented!
                // Essential part to notify about problems. Don't remove!
                onError(emitter, "fromNullable result is null");
                Log.e(TAG, "fromNullable result is null");
            }

            emitter.onComplete();
        });
    }

    public static void disposeActions(Disposable... actions) {
        if (actions != null) {
            for (Disposable action : actions) {
                boolean updateInProgress = action != null && !action.isDisposed();

                if (updateInProgress) {
                    action.dispose();
                }
            }
        }
    }

    /**
     * Fix fall back on the global error handler.
     * <a href="https://stackoverflow.com/questions/44420422/crash-when-sending-exception-through-rxjava">More info</a><br/>
     * Be aware of {@link OnErrorNotImplementedException} exception if error handler not implemented inside subscribe clause!
     */
    public static <T> void onError(ObservableEmitter<T> emitter, String msg) {
        emitter.tryOnError(new IllegalStateException(msg));
    }
}
