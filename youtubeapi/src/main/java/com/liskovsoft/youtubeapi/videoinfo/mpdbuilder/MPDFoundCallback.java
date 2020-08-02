package com.liskovsoft.youtubeapi.videoinfo.mpdbuilder;

import java.io.InputStream;

public interface MPDFoundCallback {
    void onFound(InputStream mpdPlaylist);
}
