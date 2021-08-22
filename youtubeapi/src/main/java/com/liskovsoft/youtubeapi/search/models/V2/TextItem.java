package com.liskovsoft.youtubeapi.search.models.V2;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class TextItem {
    @JsonPath("$.runs[0].text")
    private String mText1;

    @JsonPath("$.runs[1].text")
    private String mText2;

    @JsonPath("$.simpleText")
    private String mFullText;

    public String getText() {
        return ServiceHelper.combineText(mText1, mText2, mFullText);
    }
}
