package com.liskovsoft.youtubeapi.app.potokennp2.misc

import androidx.annotation.RequiresApi
import com.liskovsoft.youtubeapi.app.potokennp2.core.PoTokenGenerator
import com.liskovsoft.youtubeapi.app.potokennp2.generators.PoTokenWebView

@RequiresApi(19)
internal fun selectFactory(): PoTokenGenerator.Factory = PoTokenWebView