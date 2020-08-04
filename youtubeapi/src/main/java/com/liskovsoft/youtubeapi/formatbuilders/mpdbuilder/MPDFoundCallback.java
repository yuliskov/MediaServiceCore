package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import java.io.InputStream;

public interface MPDFoundCallback {
    void onFound(InputStream mpdPlaylist);
}
