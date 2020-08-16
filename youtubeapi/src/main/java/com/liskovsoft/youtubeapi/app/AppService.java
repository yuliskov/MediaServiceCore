package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.models.AppInfoResult;
import com.liskovsoft.youtubeapi.app.models.ClientPlaybackNonceFunctionResult;
import com.liskovsoft.youtubeapi.app.models.DecipherFunctionResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.squareup.duktape.Duktape;
import retrofit2.Call;

import java.util.Arrays;
import java.util.List;

public class AppService {
    private static AppService sInstance;
    private final AppManager mAppManager;
    private Duktape mDuktape;
    private String mCachedDecipherFunction;
    private String mCachedClientPlaybackNonceFunction;
    private String mCachedPlayerUrl;

    private AppService() {
        mAppManager = RetrofitHelper.withRegExp(AppManager.class);
    }

    /**
     * Js evaluator. Contains native *.so libs.<br/>
     * Note, lazy init for easy testing.<br/>
     * Could be tested only inside instrumented tests!
     */
    private Duktape getDuktape() {
        if (mDuktape == null) {
            mDuktape = Duktape.create(); // js evaluator, contains native *.so libs
        }

        return mDuktape;
    }

    public static AppService instance() {
        if (sInstance == null) {
            sInstance = new AppService();
        }

        return sInstance;
    }

    /**
     * Decipher strings using js code
     */
    public List<String> decipher(List<String> ciphered) {
        if (isAllNulls(ciphered)) {
            return ciphered;
        }

        String decipherCode = createDecipherCode(ciphered);

        return runDecipherCode(decipherCode);
    }

    /**
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".
     */
    public String getClientPlaybackNonce() {
        String code = createClientPlaybackNonceCode();

        if (code == null) {
            return null;
        }

        return getDuktape().evaluate(code).toString();
    }

    private static boolean isAllNulls(List<String> ciphered) {
        for (String cipher : ciphered) {
            if (cipher != null) {
                return false;
            }
        }

        return true;
    }

    private String getDecipherFunction() {
        if (mCachedDecipherFunction != null) {
            return mCachedDecipherFunction;
        }

        String playerUrl = getPlayerUrl();

        if (playerUrl != null) {
            Call<DecipherFunctionResult> decipherWrapper = mAppManager.getDecipherFunction(playerUrl);
            DecipherFunctionResult decipherFunction = RetrofitHelper.get(decipherWrapper);

            if (decipherFunction != null) {
                String content = decipherFunction.getContent();

                if (content != null) {
                    mCachedDecipherFunction = Helpers.replace(content, AppConstants.SIGNATURE_DECIPHER, "function decipherSignature");
                }
            }
        }

        return mCachedDecipherFunction;
    }

    private String getClientPlaybackNonceFunction() {
        if (mCachedClientPlaybackNonceFunction != null) {
            return mCachedClientPlaybackNonceFunction;
        }

        String playerUrl = getPlayerUrl();

        if (playerUrl != null) {
            Call<ClientPlaybackNonceFunctionResult> playbackNonceWrapper = mAppManager.getClientPlaybackNonceFunction(playerUrl);
            ClientPlaybackNonceFunctionResult playbackNonceFunction = RetrofitHelper.get(playbackNonceWrapper);

            if (playbackNonceFunction != null) {
                String content = playbackNonceFunction.getContent();

                if (content != null) {
                    mCachedClientPlaybackNonceFunction = Helpers.replace(content, AppConstants.SIGNATURE_CLIENT_PLAYBACK_NONCE, "function getClientPlaybackNonce");
                }
            }
        }

        return mCachedClientPlaybackNonceFunction;
    }

    private String getPlayerUrl() {
        if (mCachedPlayerUrl != null) {
            return mCachedPlayerUrl;
        }

        Call<AppInfoResult> wrapper = mAppManager.getAppInfo(AppConstants.USER_AGENT_SAMSUNG_1);
        AppInfoResult appInfo = RetrofitHelper.get(wrapper);

        if (appInfo != null) {
            String playerUrl = appInfo.getPlayerUrl();

            if (playerUrl != null) {
                mCachedPlayerUrl = AppConstants.SCRIPTS_URL_BASE + playerUrl.replace("\\/", "/");
            }
        }

        return mCachedPlayerUrl;
    }

    private String createDecipherCode(List<String> ciphered) {
        String decipherFunction = getDecipherFunction();

        if (decipherFunction == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        result.append(decipherFunction);
        result.append("var result = [];");

        for (String cipher : ciphered) {
            result.append(String.format("result.push(decipherSignature('%s'));", cipher));
        }

        result.append("result.toString();");

        return result.toString();
    }

    private List<String> runDecipherCode(String decipherCode) {
        String result = getDuktape().evaluate(decipherCode).toString();

        String[] values = result.split(",");

        return Arrays.asList(values);
    }

    private String createClientPlaybackNonceCode() {
        String playbackNonceFunction = getClientPlaybackNonceFunction();

        if (playbackNonceFunction == null) {
            return null;
        }

        return playbackNonceFunction + "getClientPlaybackNonce();";
    }
}
