package com.liskovsoft.youtubeapi.lounge.models.info;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.List;

public class TokenInfoList {
    @JsonPath("$.screens[*]")
    private List<TokenInfo> mTokenInfos;

    public List<TokenInfo> getTokenInfos() {
        return mTokenInfos;
    }
}
