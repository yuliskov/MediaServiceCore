package com.liskovsoft.youtubeapi.common.converters.regexpandjsonpath;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RegExpAndJsonPath {
    /**
     * Regular expression and path for the desired json element
     */
    String[] value();
}
