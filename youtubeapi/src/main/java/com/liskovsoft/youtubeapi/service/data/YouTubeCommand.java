package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.Command;
import com.liskovsoft.youtubeapi.common.helpers.AppHelper;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandItem;
import com.liskovsoft.youtubeapi.lounge.models.commands.SeekToParams;
import com.liskovsoft.youtubeapi.lounge.models.commands.PlaylistParams;

public class YouTubeCommand implements Command {
    private int mType = -1;
    private String mVideoId;
    private String mPlaylistId;
    private long mCurrentTimeMs;

    public static Command from(CommandItem info) {
        if (info == null) {
            return null;
        }

        YouTubeCommand command = new YouTubeCommand();

        switch (info.getType()) {
            case CommandItem.TYPE_SET_PLAYLIST:
                command.mType = Command.TYPE_OPEN_VIDEO;
                PlaylistParams setPlaylistParams = info.getPlaylistParams();
                command.mVideoId = setPlaylistParams.getVideoId();
                command.mPlaylistId = setPlaylistParams.getPlaylistId();
                command.mCurrentTimeMs = AppHelper.toMillis(setPlaylistParams.getCurrentTimeSec());
                break;
            case CommandItem.TYPE_SEEK_TO:
                command.mType = Command.TYPE_SEEK;
                SeekToParams seekToParams = info.getSeekToParams();
                command.mCurrentTimeMs = AppHelper.toMillis(seekToParams.getNewTimeSec());
                break;
            case CommandItem.TYPE_PLAY:
                command.mType = Command.TYPE_PLAY;
                break;
            case CommandItem.TYPE_PAUSE:
                command.mType = Command.TYPE_PAUSE;
                break;
            case CommandItem.TYPE_GET_NOW_PLAYING:
                command.mType = Command.TYPE_GET_STATE;
                break;
        }

        return command;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public String getVideoId() {
        return mVideoId;
    }

    @Override
    public String getPlaylistId() {
        return mPlaylistId;
    }

    @Override
    public long getCurrentTimeMs() {
        return mCurrentTimeMs;
    }
}
