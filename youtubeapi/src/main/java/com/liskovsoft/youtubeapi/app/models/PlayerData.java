package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.converters.FieldNullable;
import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

import java.util.regex.Pattern;

/**
 * Parser for https://www.youtube.com/s/player/11aba956/tv-player-ias.vflset/tv-player-ias.js
 */
public class PlayerData {
    // Begin ClientPlaybackNonceFunction

    private static final String FUNCTION_RANDOM_BYTES =
            "var window={};window.crypto={getRandomValues:function(arr){for(var i=0;i<arr.length;i++){arr[i]=Math.floor(Math.random()*Math.floor(Math.pow(2,8*arr.BYTES_PER_ELEMENT)))}}};";

    private static final Pattern SIGNATURE_CLIENT_PLAYBACK_NONCE_V1 = Pattern.compile("function [$\\w]+\\(\\)");
    private static final Pattern SIGNATURE_CLIENT_PLAYBACK_NONCE_V2 =
            Pattern.compile("(function [$\\w]+\\(a?\\)[\\S\\s]*)(function [$\\w]+\\(a?\\))([\\S\\s]*)");

    /**
     * Return Client Playback Nonce (CPN) function that used in tracking as string.<br/>
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".<br/>
     * Used with History and other stuff.<br/>
     * Note: [\S\s]* - match any char (including new lines) after getRandomValues<br/>
     * Player url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @FieldNullable
    @RegExp(";function [$\\w]+\\(a?\\)\\{if\\(window\\.crypto&&window\\.crypto\\.getRandomValues[\\S\\s]*?" +
            "function [$\\w]+\\(\\)\\{for\\(var .*b\\.push\\(\".*\"\\.charAt\\(.*\\)\\);return b\\.join\\(\"\"\\)\\}")
    private String mClientPlaybackNonceFunctionV1;

    /**
     * Return Client Playback Nonce (CPN) function that used in tracking as string.<br/>
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".<br/>
     * Used with History and other stuff.<br/>
     * Note: [\S\s]* - match any char (including new lines) after getRandomValues<br/>
     * Player url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp("function [$\\w]+\\(a?\\)\\{if\\(window\\.crypto&&window\\.crypto\\.getRandomValues[\\S\\s]*?" +
            "function [$\\w]+\\(a?\\)\\{.*for\\(\\w+ .*b\\.push\\(\".*\"\\.charAt\\(.*\\)\\);return b\\.join\\(\"\"\\)\\}")
    private String mClientPlaybackNonceFunctionV2;

    public String getClientPlaybackNonceFunction() {
        String cpn = getRawClientPlaybackNonceFunction();

        return cpn != null ? FUNCTION_RANDOM_BYTES + cpn + "getClientPlaybackNonce();" : null;
    }

    public String getRawClientPlaybackNonceFunction() {
        String cpn = getClientPlaybackNonceFunctionV2();

        if (cpn == null) {
            cpn = getClientPlaybackNonceFunctionV1();
        }

        return cpn;
    }

    private String getClientPlaybackNonceFunctionV1() {
        return Helpers.replace(mClientPlaybackNonceFunctionV1, SIGNATURE_CLIENT_PLAYBACK_NONCE_V1, "function getClientPlaybackNonce()");
    }

    private String getClientPlaybackNonceFunctionV2() {
        return Helpers.replace(mClientPlaybackNonceFunctionV2, SIGNATURE_CLIENT_PLAYBACK_NONCE_V2,
                "$1function getCPN(a)$3function getClientPlaybackNonce(){return getCPN(16)}");
    }

    // End ClientPlaybackNonceFunction

    // Begin DecipherFunction

    private static final Pattern SIGNATURE_DECIPHER = Pattern.compile("function [$\\w]+\\(a\\)");

    /**
     * Return JS decipher function as string.<br/>
     * Used when deciphering music items.<br/>
     * Player url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp({
        ";\\w+ [$\\w]+=\\{[\\S\\s]{10,200}?a\\.reverse\\(\\)[\\S\\s]*?function [$\\w]+\\(a\\)\\{.*a\\.split\\(\"\"\\).*;return a\\.join\\(\"\"\\)\\}",
        //";var [$\\w]+=\\{.*a\\.reverse\\(\\)[\\S\\s]*?function [$\\w]+\\(a\\)\\{.*a\\.split\\(\"\"\\).*;return a\\.join\\(\"\"\\)\\}",
        //";var [$\\w]+=\\{.*\\n.*\\n.*a\\.reverse\\(\\)[\\S\\s]*?function [$\\w]+\\(a\\)\\{.*a\\.split\\(\"\"\\).*;return a\\.join\\(\"\"\\)\\}",
        //";var [$\\w]+=\\{.*\\n.*a\\.reverse\\(\\)[\\S\\s]*?function [$\\w]+\\(a\\)\\{.*a\\.split\\(\"\"\\).*;return a\\.join\\(\"\"\\)\\}"
    })
    private String mDecipherFunction;

    public String getDecipherFunction() {
        return Helpers.replace(mDecipherFunction, SIGNATURE_DECIPHER, "function decipherSignature(a)");
    }

    // End DecipherFunction

    // Begin ThrottleFunction

    private static final Pattern SIGNATURE_THROTTLE = Pattern.compile("^;function [$\\w]+\\(a\\)");

    @FieldNullable
    @RegExp(";function [$\\w]+\\(a\\)\\{var b=a\\.split\\(\"\"\\)[\\S\\s]*?return b\\.join\\(\"\"\\)\\}")
    private String mThrottleFunction;

    public String getThrottleFunction() {
        return Helpers.replace(mThrottleFunction, SIGNATURE_THROTTLE, "function throttleSignature(a)");
    }

    // End ThrottleFunction

    // Begin SignatureTimestamp

    @RegExp("signatureTimestamp:(\\d+)")
    private String mSignatureTimestamp;

    public String getSignatureTimestamp() {
        return mSignatureTimestamp;
    }

    // End SignatureTimestamp

    // Begin PoTokenFunction

    // mPoTokenFunction1: Replace Mq(a, this.logger) with encodePoToken(a)
    //private static final String POTOKEN_ENCODE_FUNCTION = "function encodePoToken(a){return Array.from(new TextEncoder().encode(a))}"; // TextEncoder not supported by V8
    private static final String POTOKEN_ENCODE_FUNCTION = "function encodePoToken(a){return Array.from(Uint8Array.from(a.split('').map(letter => letter.charCodeAt(0))))}";
    private static final Pattern POTOKEN_ENCODE_FUNCTION_NAME = Pattern.compile("var b=[^;]+");
    private static final Pattern POTOKEN_LOGGER_NAME = Pattern.compile("this.logger[^;]+;");
    private static final String POTOKEN_EXPERIMENTS = "102307470137119736718||104417232878503778010";
    private static final Pattern POTOKEN_ARR_VAR1 = Pattern.compile("!(\\w+)");
    private static final Pattern POTOKEN_ARR_VAR2 = Pattern.compile(";(\\w+)\\[\\w\\]");

    // function getPoToken(a)
    //@RegExp("=function\\(a\\)(\\{[^\\{\\}]*DFO:Invalid[^\\{\\}]*\\})")
    private String mPoTokenFunction1;

    // function poTokenConcat(a, b)
    //@RegExp({
    //        "=function\\(a,b\\)(\\{void 0===b&&\\(b=0\\);[\\S\\s]*b\\[a>>2\\]\\+b\\[\\(a&3\\)<<4[\\S\\s]*?return c\\.join\\(\"\"\\)\\})", // web
    //        ";function [$\\w]+\\(a,b\\)(\\{void 0===b&&\\(b=0\\);[\\S\\s]*b\\[a>>2\\]\\+b\\[\\(a&3\\)<<4[\\S\\s]*?return c\\.join\\(\"\"\\)\\})", // tv
    //})
    private String mPoTokenFunction2;

    // function initPoTokenDataArr()
    //@RegExp({
    //        "\\.([$\\w]+=function\\(\\)\\{if\\(\\![\\S\\s]*\\[\"\\+/=\",\"\\+/\",\"-_=\",\"-_\\.\",\"-_\"\\][\\S\\s]*?\\}\\}\\}\\})", // web
    //        "function [$\\w]+\\(\\)\\{if\\(\\![\\S\\s]*\\[\"\\+/=\",\"\\+/\",\"-_=\",\"-_\\.\",\"-_\"\\][\\S\\s]*?\\}\\}\\}\\}", // tv
    //})
    private String mPoTokenFunction2_1;

    // poTokenConcat(genPoToken(a), 2)

    public String getPoTokenFunction() {
        if (mPoTokenFunction1 == null) {
            return null;
        }

        String poTokenFunction1 = Helpers.replace(mPoTokenFunction1, POTOKEN_ENCODE_FUNCTION_NAME, "var b = encodePoToken(a)");
        poTokenFunction1 = Helpers.replace(poTokenFunction1, POTOKEN_LOGGER_NAME, "");

        return POTOKEN_ENCODE_FUNCTION + ";function getPoToken(a)" + poTokenFunction1;
    }

    public String getPoTokenConcatFunction() {
        if (mPoTokenFunction2 == null || mPoTokenFunction2_1 == null) {
            return null;
        }

        String poTokenFunction2 = mPoTokenFunction2;
        String poTokenFunction2_1 = mPoTokenFunction2_1;

        if (!poTokenFunction2_1.startsWith("function")) {
            poTokenFunction2_1 = "var " + poTokenFunction2_1;
        }

        String var1 = Helpers.findFirst(mPoTokenFunction2_1, POTOKEN_ARR_VAR1);
        String var2 = Helpers.findFirst(mPoTokenFunction2_1, POTOKEN_ARR_VAR2);

        return "this." + var1 + " = null;this." + var2 + " = [];" + poTokenFunction2_1 + ";function poTokenConcat(a, b)" + poTokenFunction2;
    }

    public String getPoTokenResultFunction() {
        String poTokenFunction = getPoTokenFunction();
        String poTokenConcatFunction = getPoTokenConcatFunction();

        if (poTokenFunction == null || poTokenConcatFunction == null) {
            return null;
        }

        return poTokenFunction + ";" + poTokenConcatFunction + ";" +
                "function getPoTokenResult(){return poTokenConcat(getPoToken('" + POTOKEN_EXPERIMENTS + "'), 2);};getPoTokenResult();";
    }

    // End PoTokenFunction
}
