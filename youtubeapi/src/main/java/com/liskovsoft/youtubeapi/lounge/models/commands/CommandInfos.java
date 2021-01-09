package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.lounge.models.commands.Command;

import java.util.List;

public class CommandInfos {
    @JsonPath("$[*]")
    private List<Command> mCommands;

    public List<Command> getCommands() {
        return mCommands;
    }
}
