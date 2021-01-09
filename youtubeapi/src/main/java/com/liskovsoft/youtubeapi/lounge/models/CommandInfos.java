package com.liskovsoft.youtubeapi.lounge.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class CommandInfos {
    @JsonPath("$[*]")
    private List<Command> mCommands;

    public List<Command> getCommands() {
        return mCommands;
    }
}
