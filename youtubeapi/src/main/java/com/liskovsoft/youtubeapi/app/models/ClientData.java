package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;

/**
 * Data contained inside m=base js file (modern clients) or m=main js file (Cobalt/Legacy)<br/>
 * NOTE: Same pattern can be encountered 3 times or more<br/>
 * We should use the first one because other ones contain wrong client id.
 */
public class ClientData {
    /**
     *  We need first occurrence of the pattern (Android TV device).<br/>
     *  NOTE: old patterns (if match found) contain wrong client id (Http 401 error).
     */
    @RegExp({
            "clientId:\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",\\n?[$\\w]+:\"\\w+\"",
            //"var [$\\w]+=\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",\\n?[$\\w]+=\"\\w+\"",
            //"\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",[$\\w]+=\"\\w+\""
    })
    private String mClientId;

    /**
     *  We need first occurrence of the pattern (Android TV device).<br/>
     *  NOTE: old patterns (if match found) contain wrong client id (Http 401 error).
     */
    @RegExp({
            "clientId:\"[-\\w]+\\.apps\\.googleusercontent\\.com\",\\n?[$\\w]+:\"(\\w+)\"",
            //"var [$\\w]+=\"[-\\w]+\\.apps\\.googleusercontent\\.com\",\\n?[$\\w]+=\"(\\w+)\"",
            //"\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[$\\w]+=\"(\\w+)\""
    })
    private String mClientSecret;

    public String getClientId() {
        return mClientId;
    }
    
    public String getClientSecret() {
        return mClientSecret;
    }
}
