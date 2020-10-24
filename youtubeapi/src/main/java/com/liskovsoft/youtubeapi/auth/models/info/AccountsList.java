package com.liskovsoft.youtubeapi.auth.models.info;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class AccountsList {
    @JsonPath("$.contents[0].accountSectionListRenderer.contents[0].accountItemSectionRenderer.contents[*].accountItem")
    private List<AccountInt> mAccounts;

    public List<AccountInt> getAccounts() {
        return mAccounts;
    }
}
