package com.liskovsoft.youtubeapi.lounge.models.commands;

import androidx.annotation.NonNull;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.List;

public class CommandItem {
    public static final String TYPE_SET_PLAYLIST = "setPlaylist";
    public static final String TYPE_LOUNGE_STATUS = "loungeStatus";
    public static final String TYPE_REMOTE_CONNECTED = "remoteConnected";
    public static final String TYPE_REMOTE_DISCONNECTED = "remoteDisconnected";
    public static final String TYPE_GET_NOW_PLAYING = "getNowPlaying";
    public static final String TYPE_UPDATE_PLAYLIST = "updatePlaylist";
    public static final String TYPE_NOOP = "noop";
    public static final String TYPE_PLAY = "play";
    public static final String TYPE_PAUSE = "pause";
    public static final String TYPE_NEXT = "next";
    public static final String TYPE_PREVIOUS = "previous";
    public static final String TYPE_SEEK_TO = "seekTo";
    public static final String TYPE_SET_VOLUME = "setVolume";
    public static final String TYPE_STOP_VIDEO = "stopVideo";
    public static final String TYPE_DPAD = "dpadCommand";
    public static final String TYPE_VOICE = "voiceCommand";
    public static final String TYPE_SUBTITLES = "setSubtitlesTrack";
    // Special commands
    public static final String TYPE_SESSION_ID = "c";
    public static final String TYPE_G_SESSION_ID = "S";
    public static final String KEY_UP = "UP";
    public static final String KEY_DOWN = "DOWN";
    public static final String KEY_LEFT = "LEFT";
    public static final String KEY_RIGHT = "RIGHT";
    public static final String KEY_ENTER = "ENTER";
    public static final String KEY_BACK = "BACK";

    @JsonPath("$[0]")
    private int mIndex;

    @JsonPath("$[1][0]")
    private String mType;

    @JsonPath("$[1][1]")
    private PlaylistParams mPlaylistParams;

    @JsonPath("$[1][1]")
    private SeekToParams mSeekToParams;

    @JsonPath("$[1][1]")
    private RemoteParams mRemoteParams;

    @JsonPath("$[1][1]")
    private VolumeParams mVolumeParams;

    @JsonPath("$[1][1]")
    private VoiceParams mVoiceParams;

    @JsonPath("$[1][1:]")
    private List<String> mParams;

    @JsonPath("$[1][1].key")
    private String mKey;

    public int getIndex() {
        return mIndex;
    }

    public String getType() {
        return mType;
    }

    public List<String> getParams() {
        return mParams;
    }

    public PlaylistParams getPlaylistParams() {
        return mPlaylistParams;
    }

    public SeekToParams getSeekToParams() {
        return mSeekToParams;
    }

    public RemoteParams getRemoteParams() {
        return mRemoteParams;
    }

    public VolumeParams getVolumeParams() {
        return mVolumeParams;
    }

    public VoiceParams getVoiceParams() {
        return mVoiceParams;
    }

    public String getKey() {
        return mKey;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Type: %s", getType());
    }
}
