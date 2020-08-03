package com.liskovsoft.youtubeapi.videoinfo.old.mpdbuilder;

import com.liskovsoft.youtubeapi.videoinfo.old.interfaces.MediaItem;
import com.liskovsoft.youtubeapi.videoinfo.old.interfaces.Subtitle;

import java.io.InputStream;
import java.util.List;

public interface MPDBuilder {
    String VIDEO_MP4 = "video/mp4";
    String AUDIO_MP4 = "audio/mp4";
    InputStream build();
    boolean isEmpty();
    void append(MediaItem mediaItem);
    void append(List<Subtitle> subs);
    void append(Subtitle sub);
    boolean isDynamic();
    void limitVideoCodec(String codec);
    void limitAudioCodec(String codec);
}
