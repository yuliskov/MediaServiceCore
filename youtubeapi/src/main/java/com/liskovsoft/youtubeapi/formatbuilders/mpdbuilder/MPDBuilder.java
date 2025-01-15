package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat;
import com.liskovsoft.mediaserviceinterfaces.data.MediaSubtitle;

import java.io.InputStream;
import java.util.List;

public interface MPDBuilder {
    String VIDEO_MP4 = "video/mp4";
    String AUDIO_MP4 = "audio/mp4";
    InputStream build();
    boolean isEmpty();
    void append(MediaFormat mediaItem);
    void append(List<MediaSubtitle> subs);
    void append(MediaSubtitle sub);
    boolean isDynamic();
    void limitVideoCodec(String codec);
    void limitAudioCodec(String codec);
}
