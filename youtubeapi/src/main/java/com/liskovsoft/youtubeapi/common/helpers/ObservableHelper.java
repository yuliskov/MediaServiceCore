package com.liskovsoft.youtubeapi.common.helpers;

import io.reactivex.Observable;

public class ObservableHelper {
    public static Observable<Void> fromRunnable(Runnable callback) {
        return Observable.create(emitter -> {
            callback.run();
            emitter.onComplete();
        });
    }
}
