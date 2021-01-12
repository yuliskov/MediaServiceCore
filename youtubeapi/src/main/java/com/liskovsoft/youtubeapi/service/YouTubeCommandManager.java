package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.CommandManager;
import com.liskovsoft.mediaserviceinterfaces.data.Command;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.lounge.LoungeService;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandInfo;
import com.liskovsoft.youtubeapi.service.data.YouTubeCommand;
import io.reactivex.Observable;

public class YouTubeCommandManager implements CommandManager {
    private static final String TAG = YouTubeCommandManager.class.getSimpleName();
    private static YouTubeCommandManager sInstance;
    private final LoungeService mLoungeService;

    private YouTubeCommandManager() {
        mLoungeService = LoungeService.instance();

        GlobalPreferences.setOnInit(() -> {
            //mAccountManager.init();
            //this.updateAuthorizationHeader();
        });
    }

    public static YouTubeCommandManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeCommandManager();
        }

        return sInstance;
    }

    @Override
    public String getDeviceCode() {
        return mLoungeService.getPairingCode();
    }

    @Override
    public Observable<String> getDeviceCodeObserve() {
        return ObservableHelper.fromNullable(this::getDeviceCode);
    }

    @Override
    public Observable<Command> getDeviceCommandObserve() {
        return Observable.create(emitter -> {
            mLoungeService.startListening(infos -> {
                for (CommandInfo info : infos.getCommands()) {
                     emitter.onNext(YouTubeCommand.from(info));
                }
            });
        });
    }
}
