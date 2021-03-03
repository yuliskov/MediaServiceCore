package com.liskovsoft.mediaserviceinterfaces.data;

public interface Command {
    int TYPE_UNDEFINED = -1;
    int TYPE_OPEN_VIDEO = 0;
    int TYPE_SEEK = 1;
    int TYPE_PLAY = 2;
    int TYPE_PAUSE = 3;
    int TYPE_GET_STATE = 4;
    int TYPE_CONNECTED = 5;
    int TYPE_DISCONNECTED = 6;
    int TYPE_UPDATE_PLAYLIST = 7;
    int TYPE_NEXT = 8;
    int TYPE_PREVIOUS = 9;
    int TYPE_VOLUME = 10;
    int TYPE_IDLE = 11;
    int getType();
    String getVideoId();
    String getPlaylistId();
    long getCurrentTimeMs();
    String getDeviceName();
    String getDeviceId();
    int getPlaylistIndex();
    int getVolume();
}
