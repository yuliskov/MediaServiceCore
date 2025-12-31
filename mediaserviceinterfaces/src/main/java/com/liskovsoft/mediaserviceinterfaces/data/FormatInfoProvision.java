package com.liskovsoft.mediaserviceinterfaces.data;

interface FormatInfoProvision {
    boolean isSynced();
    boolean isAuth();
    String getEventId();
    String getVisitorMonitoringData();
    String getOfParam();
    String getClickTrackingParams();
    void setClickTrackingParams(String clickTrackingParams);
    boolean isCacheActual();
    void sync(MediaItemFormatInfo formatInfo);
}
