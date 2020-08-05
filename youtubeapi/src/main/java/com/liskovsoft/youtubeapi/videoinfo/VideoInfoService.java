package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.VideoFormat;

import java.util.ArrayList;
import java.util.List;

public class VideoInfoService {
    private static VideoInfoService sInstance;
    private final AppService mAppService;

    private VideoInfoService() {
        mAppService = AppService.instance();
    }

    public static VideoInfoService instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoService();
        }

        return sInstance;
    }

    private List<VideoFormat> decipherFormats(List<VideoFormat> formats) {
        List<String> ciphered = extractCipheredStrings(formats);
        List<String> deciphered = mAppService.decipher(ciphered);
        return applyDecipheredStrings(deciphered, formats);
    }

    private static List<String> extractCipheredStrings(List<VideoFormat> formats) {
        List<String> result = new ArrayList<>();

        for (VideoFormat format : formats) {
            result.add(format.getSignatureCipher());
        }

        return result;
    }

    private static List<VideoFormat> applyDecipheredStrings(List<String> deciphered, List<VideoFormat> formats) {
        if (deciphered.size() != formats.size()) {
            throw new IllegalStateException("Sizes of formats and deciphered strings should match!");
        }

        for (int i = 0; i < formats.size(); i++) {
            formats.get(i).setSignature(deciphered.get(i));
        }

        return formats;
    }
}
