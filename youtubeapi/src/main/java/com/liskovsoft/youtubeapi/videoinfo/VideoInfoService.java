package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.VideoFormat;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class VideoInfoService {
    private static final String TAG = VideoInfoService.class.getSimpleName();
    private static VideoInfoService sInstance;
    private final AppService mAppService;
    private final VideoInfoManager mVideoInfoManager;

    private VideoInfoService() {
        mAppService = AppService.instance();
        mVideoInfoManager = RetrofitHelper.withQueryString(VideoInfoManager.class);
    }

    public static VideoInfoService instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoService();
        }

        return sInstance;
    }

    private void decipherFormats(List<? extends VideoFormat> formats) {
        if (formats == null) {
            return;
        }

        List<String> ciphered = extractCipheredStrings(formats);
        List<String> deciphered = mAppService.decipher(ciphered);
        applyDecipheredStrings(deciphered, formats);
    }

    private static List<String> extractCipheredStrings(List<? extends VideoFormat> formats) {
        List<String> result = new ArrayList<>();

        for (VideoFormat format : formats) {
            result.add(format.getSignatureCipher());
        }

        return result;
    }

    private static void applyDecipheredStrings(List<String> deciphered, List<? extends VideoFormat> formats) {
        if (deciphered.size() != formats.size()) {
            throw new IllegalStateException("Sizes of formats and deciphered strings should match!");
        }

        for (int i = 0; i < formats.size(); i++) {
            formats.get(i).setSignature(deciphered.get(i));
        }
    }

    public VideoInfoResult getVideoInfo(String videoId) {
        Call<VideoInfoResult> wrapper = mVideoInfoManager.getVideoInfo(videoId);

        VideoInfoResult result = RetrofitHelper.get(wrapper);

        if (result != null) {
            decipherFormats(result.getAdaptiveFormats());
            decipherFormats(result.getRegularFormats());
        } else {
            Log.e(TAG, "Can't get video info for videoId " + videoId);
        }

        return result;
    }
}
