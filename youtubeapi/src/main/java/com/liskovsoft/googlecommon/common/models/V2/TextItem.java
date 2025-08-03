package com.liskovsoft.googlecommon.common.models.V2;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;

public class TextItem {
    @JsonPath("$.runs[0].text")
    private String mText1;

    @JsonPath("$.runs[1].text")
    private String mText2;

    @JsonPath("$.runs[2].text")
    private String mText3;

    @JsonPath("$.simpleText")
    private String mFullText;

    public CharSequence getText() {
        return ServiceHelper.combineText(mText1, mText2, mText3, mFullText);
    }

    @NonNull
    @Override
    public String toString() {
        return Helpers.toString(getText());
    }
}
