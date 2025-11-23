package com.liskovsoft.googlecommon.common.helpers;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryString;
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;
import com.liskovsoft.youtubeapi.common.models.gen.ThumbnailItem;
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.SuggestionsGroup;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

import java.util.Collections;
import java.util.List;

public final class YouTubeHelper {
    public static final String TEXT_DELIM = "•";
    public static final String TEXT_DELIM_ALT = "·";
    /**
     * NOTE: Optimal thumbnail index is 3. Lower values cause black borders around images on Chromecast and Sony.
     */
    public static final int OPTIMAL_RES_THUMBNAIL_INDEX = 3;
    //private static final int SHORTS_LEN_MS = 61_000;
    private static final int SHORTS_LEN_MS = 90_000;
    private static final int SHORTS_LEN_MAX_MS = 3 * 60_000;
    private static final String CHANNEL_KEY = "channel";
    private static final String CHANNEL_ALT_KEY = "c";
    private static final String HYPHEN_SIGN = "\u2010";

    /**
     * Find optimal thumbnail for tv screen<br/>
     * For Kotlin counterpart see: {@link com.liskovsoft.youtubeapi.common.models.gen.CommonHelperKt#getOptimalResThumbnailUrl(ThumbnailItem)}
     */
    public static String findOptimalResThumbnailUrl(List<Thumbnail> thumbnails) {
        if (thumbnails == null) {
            return null;
        }

        int size = thumbnails.size();

        if (size == 0) {
            return null;
        }

        return thumbnails.get(size > OPTIMAL_RES_THUMBNAIL_INDEX ? OPTIMAL_RES_THUMBNAIL_INDEX : size - 1).getUrl();
    }

    /**
     * For Kotlin counterpart see: {@link com.liskovsoft.youtubeapi.common.models.gen.CommonHelperKt#getHighResThumbnailUrl(ThumbnailItem)}
     */
    public static String findHighResThumbnailUrl(List<Thumbnail> thumbnails) {
        if (thumbnails == null) {
            return null;
        }

        int size = thumbnails.size();

        if (size == 0) {
            return null;
        }

        return thumbnails.get(size - 1).getUrl();
    }

    /**
     * Additional video info such as user, published etc.
     */
    public static @Nullable CharSequence createInfo(Object... items) {
        return ServiceHelper.createInfo(items);
    }

    public static String extractNextKey(MediaGroup mediaGroup) {
        String result = null;

        if (mediaGroup instanceof YouTubeMediaGroup) {
            result = ((YouTubeMediaGroup) mediaGroup).mNextPageKey;
        } else if (mediaGroup instanceof SuggestionsGroup) {
            result = ((SuggestionsGroup) mediaGroup).getNextPageKey();
        }

        return result;
    }

    public static void filterIfNeeded(MediaGroup mediaGroup) {
        if (mediaGroup == null || mediaGroup.getMediaItems() == null) {
            return;
        }

        if (mediaGroup.getType() == MediaGroup.TYPE_SUBSCRIPTIONS) {
            // LIVE & UPCOMING videos always on top
            Collections.sort(mediaGroup.getMediaItems(), (o1, o2) ->
                    o1.isLive() == o2.isLive() && o1.isUpcoming() == o2.isUpcoming() ? 0 :
                            o1.isLive() || (o1.isUpcoming() && !o2.isLive()) ? -1 : 1
            );
        }

        if (GlobalPreferences.isInitialized()) {
            MediaServiceData data = MediaServiceData.instance();
            boolean isHideShortsEnabled = (data.isContentHidden(MediaServiceData.CONTENT_SHORTS_SUBSCRIPTIONS) && mediaGroup.getType() == MediaGroup.TYPE_SUBSCRIPTIONS) ||
                    (data.isContentHidden(MediaServiceData.CONTENT_SHORTS_HOME) && mediaGroup.getType() == MediaGroup.TYPE_HOME) ||
                    (data.isContentHidden(MediaServiceData.CONTENT_SHORTS_HISTORY) && mediaGroup.getType() == MediaGroup.TYPE_HISTORY) ||
                    (data.isContentHidden(MediaServiceData.CONTENT_SHORTS_CHANNEL) && mediaGroup.getType() == MediaGroup.TYPE_CHANNEL_UPLOADS) ||
                    (data.isContentHidden(MediaServiceData.CONTENT_SHORTS_CHANNEL) && mediaGroup.getType() == MediaGroup.TYPE_CHANNEL) ||
                    (data.isContentHidden(MediaServiceData.CONTENT_SHORTS_SEARCH) && mediaGroup.getType() == MediaGroup.TYPE_SEARCH);
            boolean isHideUpcomingEnabled = (data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_SUBSCRIPTIONS) && mediaGroup.getType() == MediaGroup.TYPE_SUBSCRIPTIONS) ||
                    (data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_CHANNEL) && mediaGroup.getType() == MediaGroup.TYPE_CHANNEL_UPLOADS);
            boolean isHideStreamsEnabled = (data.isContentHidden(MediaServiceData.CONTENT_STREAMS_SUBSCRIPTIONS) && mediaGroup.getType() == MediaGroup.TYPE_SUBSCRIPTIONS);
            boolean isHideMixesEnabled = data.isContentHidden(MediaServiceData.CONTENT_MIXES);

            if (isHideShortsEnabled || isHideUpcomingEnabled || isHideStreamsEnabled || isHideMixesEnabled) {
                // NOTE: The group could be empty after filtering! Fix for that.

                // Remove Shorts and/or Upcoming
                // NOTE: Predicate replacement function for devices with Android 6.0 and below.
                Helpers.removeIf(mediaGroup.getMediaItems(), mediaItem -> {
                    if (mediaItem == null) {
                        return false;
                    }

                    return (isHideShortsEnabled && isShorts(mediaItem)) || (isHideUpcomingEnabled && mediaItem.isUpcoming()) ||
                            (isHideStreamsEnabled && mediaItem.isLive()) || (isHideMixesEnabled && isMix(mediaItem));
                });
            }
        }
    }

    private static boolean isShorts(MediaItem mediaItem) {
        if (mediaItem == null) {
            return false;
        }

        return mediaItem.isShorts() || isShortsLegacy(mediaItem);
    }

    public static boolean isShortsLegacy(MediaItem mediaItem) {
        if (mediaItem == null || mediaItem.getTitle() == null) {
            return false;
        }

        String title = mediaItem.getTitle().toLowerCase();

        long lengthMs = mediaItem.getDurationMs();

        boolean isShortsLength = lengthMs > 0 && lengthMs <= SHORTS_LEN_MS;

        if (isShortsLength) {
            return true;
        }

        boolean isShortsMaxLength = lengthMs > 0 && lengthMs <= SHORTS_LEN_MAX_MS && title.contains("#");

        if (isShortsMaxLength) {
            return true;
        }

        String imageUrl = mediaItem.getCardImageUrl();
        boolean isShortsUrl = imageUrl != null && (imageUrl.contains("-AG2CIACgA-") || imageUrl.contains("-oaymwEmCIAFEOAD8quKqQMa8AEB-AHOBYAC"));

        if (isShortsUrl) {
            return true;
        }

        return false;
    }

    public static boolean isMix(MediaItem mediaItem) {
        if (mediaItem == null || mediaItem.getTitle() == null) {
            return false;
        }

        return !mediaItem.isLive() && Helpers.hasWords(mediaItem.getBadgeText()) && mediaItem.getDurationMs() <= 0 &&
                (mediaItem.getPlaylistId() != null || mediaItem.getChannelId() != null || mediaItem.hasUploads());
    }

    /**
     * Avatar blocking fix
     */
    public static String avatarBlockFix(String url) {
        if (url != null) {
            url = url.replaceFirst("^https://yt3.ggpht.com", "https://yt4.ggpht.com");
        }

        return url;
    }

    /**
     * Exclude an items like Refresh button etc
     */
    public static boolean isEmpty(MediaItem item) {
        if (item == null) {
            return true;
        }

        return Helpers.allNulls(item.getVideoId(), item.getPlaylistId(), item.getReloadPageKey(), item.getParams(), item.getChannelId(), item.getSearchQuery());
    }

    public static int hashCodeAny(MediaItem item) {
        if (item == null) {
            return -1;
        }

        return Helpers.hashCodeAny(item.getVideoId(), item.getPlaylistId(), item.getReloadPageKey(), item.getParams(), item.getChannelId());
    }

    /**
     * Data: https://www.youtube.com/channel/UCtDjOV5nk982w35AIdVDuNw
     */
    public static String extractChannelId(Uri url) {
        if (url == null) {
            return null;
        }

        // https://youtube.com/channel/BLABLA/video
        // Don't Uri directly or you might get UnsupportedOperationException on some urls.
        UrlQueryString parser = UrlQueryStringFactory.parse(url);

        // https://youtube.com/channel/UCIy_mMwdwbC6GkRSm6gqo6Q
        String channelId = parser.get(CHANNEL_KEY);

        if (channelId == null) {
            // https://www.youtube.com/c/IngaMezerya
            channelId = parser.get(CHANNEL_ALT_KEY);

            if (channelId != null) {
                channelId = "@" + channelId; // add the prefix to quickly distinguish later
            }
        }

        if (channelId == null) {
            // https://www.youtube.com/@IngaMezerya
            String lastPathSegment = url.getLastPathSegment();

            if (Helpers.startsWith(lastPathSegment, "@")) {
                channelId = lastPathSegment; // already contains the prefix
            }
        }

        // GrayJay channel: lbry://@elissaclips#f396490429364e98d5070588aabfd039d0fc93b5
        if (Helpers.equals(url.getScheme(), "lbry")) {
            channelId = url.getAuthority(); // @elissaclips
        }

        return channelId;
    }

    /**
     * Fix truncated name after '-' sign (exo formats)
     */
    public static String exoNameFix(String lang) {
        if (lang == null) {
            return null;
        }

        return lang.replace("-", HYPHEN_SIGN);
    }

    public static boolean isGridChannel(String channelId) {
        //return Helpers.equalsAny(channelId, LIKED_MUSIC_BROWSE_ID, SUBSCRIBED_MUSIC_BROWSE_ID, PLAYED_MUSIC_BROWSE_ID);
        // NOTE: user channel starts with 'UC'
        return channelId != null && channelId.startsWith("FE");
    }

    /**
     * Try to generate a {@code t} parameter, sent by mobile clients as a query of the player
     * request.
     *
     * <p>
     * Some researches needs to be done to know how this parameter, unique at each request, is
     * generated.
     * </p>
     *
     * @return a 12 characters string to try to reproduce the {@code} parameter
     */
    @NonNull
    public static String generateTParameter() {
        return RandomStringFromAlphabetGenerator.generate(12);
    }

    /**
     * NOTE: Unique per video info instance<br/>
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".
     */
    @NonNull
    public static String generateCPNParameter() {
        return RandomStringFromAlphabetGenerator.generate(16);
    }
}
