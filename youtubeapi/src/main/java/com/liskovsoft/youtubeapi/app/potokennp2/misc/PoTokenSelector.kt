package com.liskovsoft.youtubeapi.app.potokennp2.misc

import com.liskovsoft.youtubeapi.app.potokennp2.core.PoTokenGenerator
import com.liskovsoft.youtubeapi.app.potokennp2.generators.PoTokenWebView4

internal fun selectFactory(): PoTokenGenerator.Factory = PoTokenWebView4