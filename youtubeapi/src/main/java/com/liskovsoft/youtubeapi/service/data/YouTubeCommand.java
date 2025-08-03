package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.Command;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandItem;
import com.liskovsoft.youtubeapi.lounge.models.commands.RemoteParams;
import com.liskovsoft.youtubeapi.lounge.models.commands.SeekToParams;
import com.liskovsoft.youtubeapi.lounge.models.commands.PlaylistParams;
import com.liskovsoft.youtubeapi.lounge.models.commands.VoiceParams;
import com.liskovsoft.youtubeapi.lounge.models.commands.VolumeParams;

public class YouTubeCommand implements Command {
    private int mType = Command.TYPE_UNDEFINED;
    private String mVideoId;
    private String mPlaylistId;
    private long mCurrentTimeMs;
    private String mDeviceName;
    private String mDeviceId;
    private int mPlaylistIndex;
    private int mVolume;
    private int mDelta;
    private int mKey = Command.KEY_UNDEFINED;
    private boolean mIsVoiceStarted;
    private String mSubLanguageCode;

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
                command.mCurrentTimeMs = ServiceHelper.toMillis(playlistParams.getCurrentTimeSec());
                break;
            case CommandItem.TYPE_UPDATE_PLAYLIST:
                command.mType = Command.TYPE_UPDATE_PLAYLIST;
                PlaylistParams playlistParams2 = info.getPlaylistParams();
                command.mPlaylistId = playlistParams2.getPlaylistId();
                break;
            case CommandItem.TYPE_SEEK_TO:
                command.mType = Command.TYPE_SEEK;
                SeekToParams seekToParams = info.getSeekToParams();
                command.mCurrentTimeMs = ServiceHelper.toMillis(seekToParams.getNewTimeSec());
                break;
            case CommandItem.TYPE_SET_VOLUME:
                command.mType = Command.TYPE_VOLUME;
                VolumeParams volumeParams = info.getVolumeParams();
                command.mVolume = Helpers.parseInt(volumeParams.getVolume());
                command.mDelta = Helpers.parseInt(volumeParams.getDelta());
                break;
            case CommandItem.TYPE_PLAY:
                command.mType = Command.TYPE_PLAY;
                break;
            case CommandItem.TYPE_PAUSE:
                command.mType = Command.TYPE_PAUSE;
                break;
            case CommandItem.TYPE_NEXT:
                command.mType = Command.TYPE_NEXT;
                break;
            case CommandItem.TYPE_PREVIOUS:
                command.mType = Command.TYPE_PREVIOUS;
                break;
            case CommandItem.TYPE_GET_NOW_PLAYING:
                command.mType = Command.TYPE_GET_STATE;
                break;
            case CommandItem.TYPE_STOP_VIDEO:
                command.mType = Command.TYPE_STOP;
                break;
            case CommandItem.TYPE_REMOTE_CONNECTED:
                command.mType = Command.TYPE_CONNECTED;
                RemoteParams remoteParams = info.getRemoteParams();
                command.mDeviceName = remoteParams.getDeviceName();
                command.mDeviceId = remoteParams.getDeviceId();
                break;
            case CommandItem.TYPE_REMOTE_DISCONNECTED:
                command.mType = Command.TYPE_DISCONNECTED;
                RemoteParams remoteParams2 = info.getRemoteParams();
                command.mDeviceName = remoteParams2.getDeviceName();
                command.mDeviceId = remoteParams2.getDeviceId();
                break;
            case CommandItem.TYPE_NOOP:
                command.mType = Command.TYPE_IDLE;
                break;
            case CommandItem.TYPE_DPAD:
                command.mType = Command.TYPE_DPAD;
                switch (info.getKey()) {
                    case CommandItem.KEY_UP:
                        command.mKey = Command.KEY_UP;
                        break;
                    case CommandItem.KEY_DOWN:
                        command.mKey = Command.KEY_DOWN;
                        break;
                    case CommandItem.KEY_LEFT:
                        command.mKey = Command.KEY_LEFT;
                        break;
                    case CommandItem.KEY_RIGHT:
                        command.mKey = Command.KEY_RIGHT;
                        break;
                    case CommandItem.KEY_ENTER:
                        command.mKey = Command.KEY_ENTER;
                        break;
                    case CommandItem.KEY_BACK:
                        command.mKey = Command.KEY_BACK;
                        break;
                }
                break;
            case CommandItem.TYPE_VOICE:
                command.mType = Command.TYPE_VOICE;
                VoiceParams voiceParams = info.getVoiceParams();
                command.mIsVoiceStarted = VoiceParams.STATUS_START.equals(voiceParams.getStatus());
                break;
            case CommandItem.TYPE_SUBTITLES:
                command.mType = Command.TYPE_SUBTITLES;
                PlaylistParams playlistParams3 = info.getPlaylistParams();
                command.mVideoId = playlistParams3.getVideoId();
                command.mSubLanguageCode = playlistParams3.getLanguageCode();
                break;
        }

        return command;
    }

    public String getSubLanguageCode() {
        return mSubLanguageCode;
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

    @Override
    public int getVolume() {
        return mVolume;
    }

    @Override
    public int getDelta() {
        return mDelta;
    }

    @Override
    public int getKey() {
        return mKey;
    }

    @Override
    public boolean isVoiceStarted() {
        return mIsVoiceStarted;
    }
}
