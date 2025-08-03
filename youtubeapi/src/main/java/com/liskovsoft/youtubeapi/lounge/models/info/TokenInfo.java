package com.liskovsoft.youtubeapi.lounge.models.info;

import androidx.annotation.NonNull;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class TokenInfo {
    @JsonPath("$.screenId")
    private String mScreenId;

    @JsonPath("$.loungeToken")
    private String mLoungeToken;

    public String getScreenId() {
        return mScreenId;
    }

    public String getLoungeToken() {
        return mLoungeToken;
    }

    public static TokenInfo from(String data) {
        if (data == null) {
            return null;
        }

        String[] split = data.split(",");

        TokenInfo result = new TokenInfo();
        result.mScreenId = Helpers.parseStr(split, 0);
        result.mLoungeToken = Helpers.parseStr(split, 1);

        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s,%s", mScreenId, mLoungeToken);
    }
}
