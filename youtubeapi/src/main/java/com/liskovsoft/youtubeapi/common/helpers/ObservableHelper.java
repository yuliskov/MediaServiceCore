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
                emitter.onComplete();
            } else {
                onError(emitter, "fromNullable result is null");
            }
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

    public static <T> void onError(ObservableEmitter<T> emitter, String msg) {
        try {
            // Fix fall back on the global error handler
            // More info: https://stackoverflow.com/questions/44420422/crash-when-sending-exception-through-rxjava
            emitter.tryOnError(new IllegalStateException(msg));
        } catch (OnErrorNotImplementedException e) {
            Log.e(TAG, "Oops. I forgot to add error handler somewhere. But it's ok. %s", e.getMessage());
        }
    }
}
