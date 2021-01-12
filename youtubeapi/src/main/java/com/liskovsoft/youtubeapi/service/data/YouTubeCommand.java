package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.Command;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandInfo;
import com.liskovsoft.youtubeapi.lounge.models.commands.PlaylistData;

public class YouTubeCommand implements Command {
    private int mType;
    private String mVideoId;

    public static Command from(CommandInfo info) {
        YouTubeCommand command = new YouTubeCommand();
        PlaylistData playlistData = info.getPlaylistData();
        if (playlistData != null) {
            command.mVideoId = playlistData.getVideoId();
            command.mType = Command.TYPE_OPEN;
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
}
