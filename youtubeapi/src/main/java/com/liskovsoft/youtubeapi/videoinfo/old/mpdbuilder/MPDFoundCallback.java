package com.liskovsoft.youtubeapi.videoinfo.old.mpdbuilder;

import java.io.InputStream;

public interface MPDFoundCallback {
    void onFound(InputStream mpdPlaylist);
}
