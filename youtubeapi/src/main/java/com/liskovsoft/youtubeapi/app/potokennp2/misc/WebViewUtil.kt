package com.liskovsoft.youtubeapi.app.potokennp2.misc

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.PowerManager
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import com.liskovsoft.sharedutils.mylogger.Log

private const val TAG = "WebViewUtil"

internal const val potLibPrefix = "potokennp2/"

internal fun hasThermalServiceBug(context: Context): Boolean {
    // Only Android 10 has the issue
    if (Build.VERSION.SDK_INT != 29)
        return false

    val powerService = context.getSystemService(Context.POWER_SERVICE) as? PowerManager ?: return false

    return !ThermalServiceApi29.isAvailable(powerService)
}

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
@Keep
@RequiresApi(29)
private object ThermalServiceApi29 {
    fun isAvailable(powerService: PowerManager): Boolean {
        val listener = PowerManager.OnThermalStatusChangedListener {
            // NOP
        }

        return try {
            powerService.addThermalStatusListener(listener)
            powerService.removeThermalStatusListener(listener)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Thermal service check failed", e)
            false
        }
    }
}

/**
 * NOTE: You will need to declare the android.permission.USB permission in your AndroidManifest.xml
 *
 * Trying to fix: NullPointerException: 'int android.hardware.usb.UsbDevice.getInterfaceCount()' on a null object reference
 */
internal fun hasUsbServiceBug(context: Context): Boolean {
    // Only Android 12 and up has the issue
    if (Build.VERSION.SDK_INT != 31)
        return false

    val manager = context.getSystemService(Context.USB_SERVICE) as? UsbManager ?: return false

    val deviceList = manager.getDeviceList()
    if (deviceList == null || deviceList.isEmpty()) {
        return false
    }

    for (device in deviceList.values) {
        if (device == null) {
            Log.e(TAG, "Found a null USB device in the system's device list")
            return true
        }

        try {
            val interfaceCount = device.interfaceCount
            Log.d(TAG, "Device: " + device.deviceName + ", Interface Count: " + interfaceCount)

            // You could add more checks here, e.g., for specific interfaces
            // or other properties that might be null.
        } catch (e: Exception) {
            return true
        }
    }

    return false
}

/**
 * API 19+: executes JS and returns result via callback.
 *
 * API < 19: executes JS without result (callback ignored).
 */
internal fun WebView.evaluateJavascriptLegacy(script: String, resultCallback: ValueCallback<String>?) {
    if (Build.VERSION.SDK_INT >= 19) {
        evaluateJavascript(script, resultCallback)
    } else {
        // NOTE: callbacks not supported. Use jsInterface instead
        loadUrl("javascript:(function() { $script })();")
    }
}