package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.Command;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.helpers.AppHelper;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandItem;
import com.liskovsoft.youtubeapi.lounge.models.commands.RemoteParams;
import com.liskovsoft.youtubeapi.lounge.models.commands.SeekToParams;
import com.liskovsoft.youtubeapi.lounge.models.commands.PlaylistParams;

public class YouTubeCommand implements Command {
    private int mType = -1;
    private String mVideoId;
    private String mPlaylistId;
    private long mCurrentTimeMs;
    private String mDeviceName;
    private String mDeviceId;
    private int mPlaylistIndex;

    public static Command from(CommandItem info) {
        if (info == null) {
            return null;
        }

        YouTubeCommand command = new YouTubeCommand();

        switch (info.getType()) {
            case CommandItem.TYPE_SET_PLAYLIST:
                command.mType = Command.TYPE_OPEN_VIDEO;
                PlaylistParams playlistParams = info.getPlaylistParams();
                command.mVideoId = playlistParams.getVideoId();
                command.mPlaylistId = playlistParams.getPlaylistId();
                command.mPlaylistIndex = Helpers.parseInt(playlistParams.getPlaylistIndex());
                command.mCurrentTimeMs = AppHelper.toMillis(playlistParams.getCurrentTimeSec());
                break;
            case CommandItem.TYPE_UPDATE_PLAYLIST:
                command.mType = Command.TYPE_UPDATE_PLAYLIST;
                playlistParams = info.getPlaylistParams();
                command.mPlaylistId = playlistParams.getPlaylistId();
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
            case CommandItem.TYPE_REMOTE_CONNECTED:
                command.mType = Command.TYPE_CONNECTED;
                RemoteParams remoteParams = info.getRemoteParams();
                command.mDeviceName = remoteParams.getDeviceName();
                command.mDeviceId = remoteParams.getDeviceId();
                break;
            case CommandItem.TYPE_REMOTE_DISCONNECTED:
                command.mType = Command.TYPE_DISCONNECTED;
                remoteParams = info.getRemoteParams();
                command.mDeviceName = remoteParams.getDeviceName();
                command.mDeviceId = remoteParams.getDeviceId();
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
    public int getPlaylistIndex() {
        return mPlaylistIndex;
    }

    @Override
    public long getCurrentTimeMs() {
        return mCurrentTimeMs;
    }

    @Override
    public String getDeviceName() {
        return mDeviceName;
    }

    @Override
    public String getDeviceId() {
        return mDeviceId;
    }
}
