package com.liskovsoft.googlecommon.common.models.auth.info;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.List;

public class AccountsList {
    @JsonPath("$.contents[0].accountSectionListRenderer.contents[0].accountItemSectionRenderer.contents[*].accountItem")
    private List<AccountInt> mAccounts;

    public List<AccountInt> getAccounts() {
        return mAccounts;
    }
}
