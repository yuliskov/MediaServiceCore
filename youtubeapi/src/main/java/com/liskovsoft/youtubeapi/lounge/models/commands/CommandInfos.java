package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class CommandInfos {
    @JsonPath("$[*]")
    private List<CommandInfo> mCommands;

    public List<CommandInfo> getCommands() {
        return mCommands;
    }

    public String getParam(String commandName) {
        if (mCommands == null) {
            return null;
        }

        for (CommandInfo command : mCommands) {
            if (commandName.equals(command.getType())) {
                return command.getParams().get(0);
            }
        }

        return null;
    }
}
