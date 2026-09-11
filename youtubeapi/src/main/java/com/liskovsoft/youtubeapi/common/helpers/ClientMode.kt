package com.liskovsoft.youtubeapi.common.helpers

/**
 * Device-driven client mode.
 *
 * This is the single switch point that decides which InnerTube client
 * "screen" drives the app:
 *  - On a real Android TV / set-top box the app behaves as the YouTube TV app
 *    (TVHTML5 screen).
 *  - On phone/tablet hardware it reports and behaves as the Android mobile
 *    app instead.
 *
 * The decision is based on the real device ([DeviceInfo.isTVDevice]) and can
 * be overridden here if needed.
 */
internal object ClientMode {
    val isTVDevice: Boolean get() = DeviceInfo.isTVDevice

    /** Whether the app should behave as the YouTube TV app. */
    val isTVMode: Boolean get() = isTVDevice

    /** Whether the app should behave as the Android mobile app. */
    val isPhoneMode: Boolean get() = !isTVDevice
}