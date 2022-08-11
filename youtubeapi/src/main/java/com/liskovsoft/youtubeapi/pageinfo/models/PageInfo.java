package com.liskovsoft.youtubeapi.pageinfo.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.converter.JsonPathConverterFactory;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathSkipTypeAdapter;
import com.liskovsoft.youtubeapi.common.converters.regexpandjsonpath.RegExpAndJsonPath;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class PageInfo {

    @RegExpAndJsonPath({"translationLanguages.:(.+?\\])", "$[*]"})
    private List<TranslationLanguage> mTranslationLanguages;

    public List<TranslationLanguage> getTranslationLanguages() {
        return mTranslationLanguages;
    }
}
