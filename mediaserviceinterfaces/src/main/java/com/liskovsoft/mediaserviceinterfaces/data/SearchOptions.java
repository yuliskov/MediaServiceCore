package com.liskovsoft.mediaserviceinterfaces.data;

public interface SearchOptions {
    int UPLOAD_DATE_LAST_HOUR = 0b1;
    int UPLOAD_DATE_TODAY = 0b10;
    int UPLOAD_DATE_THIS_WEEK = 0b100;
    int UPLOAD_DATE_THIS_MONTH = 0b1000;
    int UPLOAD_DATE_THIS_YEAR = 0b10000;
    int FEATURES_4K = 0b100000;
    int SORT_BY_UPLOAD_DATE = 0b1000000;
}
