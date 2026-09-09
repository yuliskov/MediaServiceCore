package com.liskovsoft.youtubeapi.app.potokennp2.misc

import android.os.PowerManager

/**
 * Isolates every access to [PowerManager.OnThermalStatusChangedListener] (API 29+).
 *
 * Why this class exists: Kotlin compiles the listener lambda into its own synthetic
 * class that implements [PowerManager.OnThermalStatusChangedListener]. When that
 * lambda sits directly inside WebViewUtil.kt, ART resolves the synthetic class while
 * executing hasThermalServiceBug() - before the SDK_INT guard can return - and throws
 * NoClassDefFoundError on every device below API 29 (observed on Fire OS 6 / Android
 * 7.1). NoClassDefFoundError is an Error, not an Exception, so none of the callers
 * catch it and the whole poToken generation aborts. Without a poToken the player
 * request returns null for every client, which surfaces as an endless
 * "Can't get video info" retry loop.
 *
 * Keeping the API usage in a separate class means it is only loaded when the code is
 * actually called, i.e. on Android 10 only.
 */
internal object ThermalServiceCheck {
    fun hasBug(powerService: PowerManager): Boolean {
        val listener = PowerManager.OnThermalStatusChangedListener {
            // NOP
        }

        return try {
            powerService.addThermalStatusListener(listener)
            false
        } catch (e: Exception) {
            true
        } finally {
            powerService.removeThermalStatusListener(listener)
        }
    }

    fun isAvailable(powerService: PowerManager): Boolean = !hasBug(powerService)
}
