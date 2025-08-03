package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.models.V2.TextItem;

public class TranslationLanguage {
    @JsonPath("$.languageCode")
    private String mLanguageCode;

    @JsonPath("$.languageName")
    private TextItem mLanguageName;

    public String getLanguageCode() {
        return mLanguageCode;
    }

    public String getLanguageName() {
        return mLanguageName != null ? mLanguageName.toString() : null;
    }
}
