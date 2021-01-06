package com.liskovsoft.youtubeapi.common.helpers;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.Callable;

public class ObservableHelper {
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
                // Fix fall back on the global error handler
                // More info: https://stackoverflow.com/questions/44420422/crash-when-sending-exception-through-rxjava
                if (!emitter.isDisposed()) {
                    emitter.onError(new IllegalStateException("Unknown error. Empty result."));
                }
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
}
