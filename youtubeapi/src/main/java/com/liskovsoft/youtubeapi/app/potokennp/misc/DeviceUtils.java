package com.liskovsoft.youtubeapi.app.potokennp.misc;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.WebSettings;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

public final class DeviceUtils {

    private static final String AMAZON_FEATURE_FIRE_TV = "amazon.hardware.fire_tv";
    private static final boolean SAMSUNG = Build.MANUFACTURER.equals("samsung");
    private static Boolean isTV = null;
    private static Boolean isFireTV = null;

    /**
     * <p>The app version code that corresponds to the last update
     * of the media tunneling device blacklist.</p>
     * <p>The value of this variable needs to be updated everytime a new device that does not
     * support media tunneling to match the <strong>upcoming</strong> version code.</p>
     * @see #shouldSupportMediaTunneling()
     */
    public static final int MEDIA_TUNNELING_DEVICE_BLACKLIST_VERSION = 994;

    // region: devices not supporting media tunneling / media tunneling blacklist
    /**
     * <p>Formuler Z8 Pro, Z8, CC, Z Alpha, Z+ Neo.</p>
     * <p>Blacklist reason: black screen</p>
     * <p>Board: HiSilicon Hi3798MV200</p>
     */
    private static final boolean HI3798MV200 = Build.VERSION.SDK_INT == 24
            && Build.DEVICE.equals("Hi3798MV200");
    /**
     * <p>Zephir TS43UHD-2.</p>
     * <p>Blacklist reason: black screen</p>
     */
    private static final boolean CVT_MT5886_EU_1G = Build.VERSION.SDK_INT == 24
            && Build.DEVICE.equals("cvt_mt5886_eu_1g");
    /**
     * Hilife TV.
     * <p>Blacklist reason: black screen</p>
     */
    private static final boolean REALTEKATV = Build.VERSION.SDK_INT == 25
            && Build.DEVICE.equals("RealtekATV");
    /**
     * <p>Phillips 4K (O)LED TV.</p>
     * Supports custom ROMs with different API levels
     */
    private static final boolean PH7M_EU_5596 = Build.VERSION.SDK_INT >= 26
            && Build.DEVICE.equals("PH7M_EU_5596");
    /**
     * <p>Philips QM16XE.</p>
     * <p>Blacklist reason: black screen</p>
     */
    private static final boolean QM16XE_U = Build.VERSION.SDK_INT == 23
            && Build.DEVICE.equals("QM16XE_U");
    /**
     * <p>Sony Bravia VH1.</p>
     * <p>Processor: MT5895</p>
     * <p>Blacklist reason: fullscreen crash / stuttering</p>
     */
    private static final boolean BRAVIA_VH1 = Build.VERSION.SDK_INT == 29
            && Build.DEVICE.equals("BRAVIA_VH1");
    /**
     * <p>Sony Bravia VH2.</p>
     * <p>Blacklist reason: fullscreen crash; this includes model A90J as reported in
     * <a href="https://github.com/TeamNewPipe/NewPipe/issues/9023#issuecomment-1387106242">
     * #9023</a></p>
     */
    private static final boolean BRAVIA_VH2 = Build.VERSION.SDK_INT == 29
            && Build.DEVICE.equals("BRAVIA_VH2");
    /**
     * <p>Sony Bravia Android TV platform 2.</p>
     * Uses a MediaTek MT5891 (MT5596) SoC.
     * @see <a href="https://github.com/CiNcH83/bravia_atv2">
     *     https://github.com/CiNcH83/bravia_atv2</a>
     */
    private static final boolean BRAVIA_ATV2 = Build.DEVICE.equals("BRAVIA_ATV2");
    /**
     * <p>Sony Bravia Android TV platform 3 4K.</p>
     * <p>Uses ARM MT5891 and a {@link #BRAVIA_ATV2} motherboard.</p>
     *
     * @see <a href="https://browser.geekbench.com/v4/cpu/9101105">
     *     https://browser.geekbench.com/v4/cpu/9101105</a>
     */
    private static final boolean BRAVIA_ATV3_4K = Build.DEVICE.equals("BRAVIA_ATV3_4K");
    /**
     * <p>Panasonic 4KTV-JUP.</p>
     * <p>Blacklist reason: fullscreen crash</p>
     */
    private static final boolean TX_50JXW834 = Build.DEVICE.equals("TX_50JXW834");
    /**
     * <p>Bouygtel4K / Bouygues Telecom Bbox 4K.</p>
     * <p>Blacklist reason: black screen; reported at
     * <a href="https://github.com/TeamNewPipe/NewPipe/pull/10122#issuecomment-1638475769">
     *     #10122</a></p>
     */
    private static final boolean HMB9213NW = Build.DEVICE.equals("HMB9213NW");
    // endregion

    private DeviceUtils() {
    }

    public static boolean isConfirmKey(final int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_SPACE:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                return true;
            default:
                return false;
        }
    }

    public static int dpToPx(@Dimension(unit = Dimension.DP) final int dp,
                             @NonNull final Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics());
    }

    public static int spToPx(@Dimension(unit = Dimension.SP) final int sp,
                             @NonNull final Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                context.getResources().getDisplayMetrics());
    }

    public static boolean isLandscape(final Context context) {
        return context.getResources().getDisplayMetrics().heightPixels < context.getResources()
                .getDisplayMetrics().widthPixels;
    }

    public static boolean hasAnimationsAnimatorDurationEnabled(final Context context) {
        return Settings.System.getFloat(
                context.getContentResolver(),
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1F) != 0F;
    }

    /**
     * <p>Some devices have broken tunneled video playback but claim to support it.</p>
     * <p>This can cause a black video player surface while attempting to play a video or
     * crashes while entering or exiting the full screen player.
     * The issue effects Android TVs most commonly.
     * See <a href="https://github.com/TeamNewPipe/NewPipe/issues/5911">#5911</a> and
     * <a href="https://github.com/TeamNewPipe/NewPipe/issues/9023">#9023</a> for more info.</p>
     * @Note Update {@link #MEDIA_TUNNELING_DEVICE_BLACKLIST_VERSION}
     * when adding a new device to the method.
     * @return {@code false} if affected device; {@code true} otherwise
     */
    public static boolean shouldSupportMediaTunneling() {
        // Maintainers note: update MEDIA_TUNNELING_DEVICES_UPDATE_APP_VERSION_CODE
        return !HI3798MV200
                && !CVT_MT5886_EU_1G
                && !REALTEKATV
                && !QM16XE_U
                && !BRAVIA_VH1
                && !BRAVIA_VH2
                && !BRAVIA_ATV2
                && !BRAVIA_ATV3_4K
                && !PH7M_EU_5596
                && !TX_50JXW834
                && !HMB9213NW;
    }

    /**
     * @return whether the device has support for WebView, see
     * <a href="https://stackoverflow.com/a/69626735">https://stackoverflow.com/a/69626735</a>
     */
    public static boolean supportsWebView() {
        try {
            CookieManager.getInstance();
            return true;
        } catch (final Throwable ignored) {
            return false;
        }
    }

    public static void setSafeBrowsingEnabled(@NonNull WebSettings settings, boolean enabled) {
        if (WebViewFeature.isFeatureSupported(WebViewFeature.SAFE_BROWSING_ENABLE)) {
            try {
                WebSettingsCompat.setSafeBrowsingEnabled(settings, enabled);
            } catch (AbstractMethodError e) { // Sometimes happens on Android 8/9
                e.printStackTrace();
                //getAdapter(settings).setSafeBrowsingEnabled(enabled); // try alt approach from WebSettingsCompat
            }
        }
    }
}
