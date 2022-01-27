package com.liskovsoft.youtubeapi.app.models.clientdata;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

/**
 * Data contained withing m=base js file<br/>
 * NOTE: Same pattern could be encountered 3 times or more<br/>
 * We're using the first one.
 */
public class ModernClientData implements ClientData {
    /**
     *  We need first occurrence of the pattern (Android TV device).<br/>
     *  Move newest patterns to the top to improve memory usage.
     */
    @RegExp({
            "clientId:\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",\\w+:\"\\w+\"",
            "var [$\\w]+=\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",\\n?[$\\w]+=\"\\w+\"",
            "\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",[$\\w]+=\"\\w+\""
    })
    private String mClientId;

    /**
     *  We need first occurrence of the pattern (Android TV device).<br/>
     *  Move newest patterns to the top to improve memory usage.
     */
    @RegExp({
            "clientId:\"[-\\w]+\\.apps\\.googleusercontent\\.com\",\\w+:\"(\\w+)\"",
            "var [$\\w]+=\"[-\\w]+\\.apps\\.googleusercontent\\.com\",\\n?[$\\w]+=\"(\\w+)\"",
            "\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[$\\w]+=\"(\\w+)\""
    })
    private String mClientSecret;

    @Override
    public String getClientId() {
        return mClientId;
    }

    @Override
    public String getClientSecret() {
        return mClientSecret;
    }
}
