package com.liskovsoft.youtubeapi.lounge.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

/**
 * Example: [0,["c","F052B2115F9479B9","",8]
 */
public class Command {
    @JsonPath("$[0]")
    private int mCommandIndex;

    @JsonPath("$[1][0]")
    private String mCommandName;

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

    //public String getScreenId() {
    //    return null;
    //}
    //
    //public String getSessionId() {
    //    return null;
    //}
}
