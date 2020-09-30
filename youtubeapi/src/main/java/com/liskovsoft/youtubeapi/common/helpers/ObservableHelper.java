package com.liskovsoft.youtubeapi.common.helpers;

import io.reactivex.Observable;

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
            }

            emitter.onComplete();
        });
    }
}
