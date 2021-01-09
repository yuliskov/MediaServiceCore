package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.lounge.models.commands.PlaylistData;

import java.util.List;

/**
 * Example: [0,["c","F052B2115F9479B9","",8]
 */
public class Command {
    public static final String TYPE_PLAYLIST = "setPlaylist";
    public static final String TYPE_NOW_PLAYING = "getNowPlaying";
    public static final String TYPE_SESSION_ID = "c";
    public static final String TYPE_GOOGLE_SESSION_ID = "S";

    @JsonPath("$[0]")
    private int mCommandIndex;

    @JsonPath("$[1][0]")
    private String mCommandName;

    @JsonPath("$[1][1]")
    private PlaylistData mPlaylistData;

    @JsonPath("$[1][1:]")
    private List<String> mCommandParams;

    public int getCommandIndex() {
        return mCommandIndex;
    }

    public String getCommandName() {
        return mCommandName;
    }

    public List<String> getCommandParams() {
        return mCommandParams;
    }

    public PlaylistData getPlaylistData() {
        return mPlaylistData;
    }

    //public String getScreenId() {
    //    return null;
    //}
    //
    //public String getSessionId() {
    //    return null;
    //}
}
