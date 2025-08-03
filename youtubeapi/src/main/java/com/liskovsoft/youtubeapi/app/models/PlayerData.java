package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.converters.FieldNullable;
import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;

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
            Pattern.compile("(function [$\\w]+\\(([\\w])\\)[\\S\\s]*)(function [$\\w]+\\([\\w]\\))([\\S\\s]*)");

    /**
     * Return Client Playback Nonce (CPN) function that used in tracking as string.<br/>
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".<br/>
     * Used with History and other stuff.<br/>
     * Note: [\S\s]* - match any char (including new lines) after getRandomValues<br/>
     * Player url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @FieldNullable
    @RegExp(";function [$\\w]+\\([\\w]?\\)\\{if\\(window\\.crypto&&window\\.crypto\\.getRandomValues[\\S\\s]*?" +
            "function [$\\w]+\\(\\)\\{for\\(var .*[\\w]\\.push\\(\".*\"\\.charAt\\(.*\\)\\);return [\\w]\\.join\\(\"\"\\)\\}")
    private String mClientPlaybackNonceFunctionV1;

    /**
     * Return Client Playback Nonce (CPN) function that used in tracking as string.<br/>
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".<br/>
     * Used with History and other stuff.<br/>
     * Note: [\S\s]* - match any char (including new lines) after getRandomValues<br/>
     * Player url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp("function [$\\w]+\\([\\w]?\\)\\{if\\(window\\.crypto&&window\\.crypto\\.getRandomValues[\\S\\s]*?" +
            "function [$\\w]+\\([\\w]?\\)\\{.*for\\(\\w+ .*[\\w]\\.push\\(\".*\"\\.charAt\\(.*\\)\\);return [\\w]\\.join\\(\"\"\\)\\}")
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
                "$1function getCPN($2)$4function getClientPlaybackNonce(){return getCPN(16)}");
    }

    // End ClientPlaybackNonceFunction

    // Begin DecipherFunction

    private static final Pattern SIGNATURE_DECIPHER = Pattern.compile("function [$\\w]+\\(([\\w])\\)");

    /**
     * Return JS decipher function as string.<br/>
     * Used when deciphering music items.<br/>
     * Player url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp({
        ";\\w+ [$\\w]+=\\{[\\S\\s]{10,200}?[\\w]\\.reverse\\(\\)[\\S\\s]*?function [$\\w]+\\([\\w]\\)\\{.*[\\w]\\.split\\(\"\"\\).*;return [\\w]\\.join\\(\"\"\\)\\}",
    })
    private String mDecipherFunction;

    @RegExp({
            ";\\w+ [$\\w]+=\\{[\\S\\s]{10,200}?[\\w]\\.reverse\\(\\)[\\S\\s]*?function [$\\w]+\\([\\w]\\)\\{.*[\\w]\\.split\\(.+\\).*;return [\\w]\\.join\\([$\\w]+\\[\\d+\\]\\)\\}",
    })
    private String mDecipherFunctionPart1;

    @RegExp({
            "'use strict';(var [$\\w]+=[.\\S\\s]+?\\.split\\(.+?\\);)",
    })
    private String mDecipherFunctionPart2;

    public String getDecipherFunction() {
        String deFunc = null;

        if (mDecipherFunction != null) {
            deFunc = Helpers.replace(mDecipherFunction, SIGNATURE_DECIPHER, "function decipherSignature($1)");
        } else if (mDecipherFunctionPart1 != null && mDecipherFunctionPart2 != null) {
            deFunc = Helpers.replace(mDecipherFunctionPart1, SIGNATURE_DECIPHER, "function decipherSignature($1)") + ";" + mDecipherFunctionPart2;
        }

        return deFunc;
    }

    // End DecipherFunction

    // Begin SignatureTimestamp

    @RegExp("signatureTimestamp:(\\d+)")
    private String mSignatureTimestamp;

    public String getSignatureTimestamp() {
        return mSignatureTimestamp;
    }

    // End SignatureTimestamp
}
