package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaService;

public class YouTubeMediaServiceFactory {
    public static MediaService create() {
        YouTubeSignInManager authManager = YouTubeSignInManager.instance();

        if (authManager.isSigned()) {
            return YouTubeMediaServiceSigned.instance();
        } else {
            return YouTubeMediaService.instance();
        }
    }
}
