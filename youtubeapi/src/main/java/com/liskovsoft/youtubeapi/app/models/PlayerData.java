package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.common.converters.FieldNullable;
import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

import java.util.regex.Pattern;

/**
 * Parser for https://www.youtube.com/s/player/11aba956/tv-player-ias.vflset/tv-player-ias.js
 */
public class PlayerData {
    private static final Pattern SIGNATURE_DECIPHER = Pattern.compile("function [$\\w]+\\(a\\)");
    private static final Pattern SIGNATURE_CLIENT_PLAYBACK_NONCE_V1 = Pattern.compile("function [$\\w]+\\(\\)");
    private static final Pattern SIGNATURE_CLIENT_PLAYBACK_NONCE_V2 =
            Pattern.compile("(function [$\\w]+\\(a?\\)[\\S\\s]*)(function [$\\w]+\\(a?\\))([\\S\\s]*)");
    private static final Pattern SIGNATURE_THROTTLE = Pattern.compile("^;function [$\\w]+\\(a\\)");

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

    @RegExp(";function [$\\w]+\\(a\\)\\{var b=a\\.split\\(\"\"\\)[\\S\\s]*?return b\\.join\\(\"\"\\)\\}")
    private String mThrottleFunction;

    @RegExp("signatureTimestamp:(\\d+)")
    private String mSignatureTimestamp;

    public String getDecipherFunction() {
        return Helpers.replace(mDecipherFunction, SIGNATURE_DECIPHER, "function decipherSignature(a)");
    }

    public String getClientPlaybackNonceFunction() {
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

    public String getThrottleFunction() {
        return Helpers.replace(mThrottleFunction, SIGNATURE_THROTTLE, "function throttleSignature(a)");
    }

    public String getSignatureTimestamp() {
        return mSignatureTimestamp;
    }
}
