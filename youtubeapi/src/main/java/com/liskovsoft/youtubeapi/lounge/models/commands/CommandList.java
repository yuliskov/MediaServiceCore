package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.List;

public class CommandList {
    @JsonPath("$[*]")
    private List<CommandItem> mCommands;

    public List<CommandItem> getCommands() {
        return mCommands;
    }

    public String getParam(String commandName) {
        if (mCommands == null) {
            return null;
        }

        for (CommandItem command : mCommands) {
            if (commandName.equals(command.getType())) {
                return command.getParams().get(0);
            }
        }

        return null;
    }
}
