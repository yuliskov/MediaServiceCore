package com.liskovsoft.youtubeapi.pageinfo.models;

import com.liskovsoft.youtubeapi.common.converters.regexpandjsonpath.RegExpAndJsonPath;
import com.liskovsoft.youtubeapi.common.models.V2.TextItem;

public class TranslationLanguage {
    /**
     * Example: "en"
     */
    @RegExpAndJsonPath({"()", "$.languageCode"})
    private String mLanguageCode;
    @RegExpAndJsonPath({"()", "$.languageName.simpleText"})
    private String mLanguageName;

    public String getLanguageCode() {
        return mLanguageCode;
    }

    public String getLanguageName() {
        return mLanguageName;
    }

}
