package com.liskovsoft.youtubeapi.common.helpers

import android.os.Build
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.app.AppService

/**
 * Identity of the actual Android device this app runs on.
 *
 * Used to report the real hardware to YouTube's InnerTube API instead of
 * impersonating a spoofed client device.
 */
internal object DeviceInfo {
    val sdkVersion: Int = Build.VERSION.SDK_INT
    val osName: String = "Android"
    val osVersion: String = Build.VERSION.RELEASE ?: ""
    val deviceMake: String = Build.MANUFACTURER ?: ""
    val deviceModel: String = Build.MODEL ?: ""
    val deviceId: String = Build.DEVICE ?: ""
    val buildId: String = Build.ID ?: ""
    val brand: String = Build.BRAND ?: ""
    val hardware: String = Build.HARDWARE ?: ""

    /**
     * Whether the real device is an Android TV / set-top box.
     *
     * Used as the switch point that selects which identity form factor is
     * reported to the InnerTube API (ATV vs generic phone/tablet).
     */
    val isTVDevice: Boolean by lazy {
        // Relies on the leanback/television system features when the context is available
        try {
            Helpers.isAndroidTV(AppService.instance().getContext())
        } catch (e: Exception) {
            // Context isn't initialized yet (e.g. early startup or unit tests).
            // Build.IS_TELEVISION isn't part of the public SDK, so read it via reflection.
            try {
                Build::class.java.getField("IS_TELEVISION").getBoolean(null)
            } catch (e: Exception) {
                false
            }
        }
    }
}