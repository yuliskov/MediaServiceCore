package com.liskovsoft.googlecommon.common.models.auth;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class ErrorResponse {
    @JsonPath("$.error")
    private String mError;

    @JsonPath("$.error_description")
    private String mErrorDescription;

    public String getError() {
        return mError;
    }

    public String getErrorDescription() {
        return mErrorDescription;
    }
}
