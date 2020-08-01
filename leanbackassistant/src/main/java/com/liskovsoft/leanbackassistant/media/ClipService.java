package com.liskovsoft.leanbackassistant.media;

import android.content.Context;
import androidx.tvprovider.media.tv.TvContractCompat;
import com.liskovsoft.leanbackassistant.R;
import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceSigned;

import java.util.ArrayList;
import java.util.List;

public class ClipService {
    private static final int SUBSCRIPTIONS_ID = 1;
    private static final int HISTORY_ID = 2;
    private static final int RECOMMENDED_ID = 3;
    private static final String SUBS_CHANNEL_ID = "subs_channel_id";
    private static final String SUBS_PROGRAMS_IDS = "subs_clips_ids";
    private static final String RECOMMENDED_CHANNEL_ID = "recommended_channel_id";
    private static final String RECOMMENDED_PROGRAMS_IDS = "recommended_programs_ids";
    private static final String HISTORY_CHANNEL_ID = "history_channel_id";
    private static final String HISTORY_PROGRAMS_IDS = "history_programs_ids";
    private static final String SUBSCRIPTIONS_URL = "https://www.youtube.com/tv#/zylon-surface?c=FEsubscriptions&resume";
    private static final String HISTORY_URL = "https://www.youtube.com/tv#/zylon-surface?c=FEmy_youtube&resume";
    private static final String RECOMMENDED_URL = "https://www.youtube.com/tv#/zylon-surface?c=default&resume";
    private static ClipService mInstance;
    private final Context mContext;

    public ClipService(Context context) {
        mContext = context;
    }

    public static ClipService instance(Context context) {
        if (mInstance == null) {
            mInstance = new ClipService(context);
        }

        return mInstance;
    }

    public Playlist getSubscriptionsPlaylist() {
        YouTubeMediaServiceSigned service = YouTubeMediaServiceSigned.instance();
        MediaTab subscriptions = service.getSubscriptions();
        List<MediaItem> mediaItems = subscriptions.getMediaItems();

        Playlist playlist = new Playlist(
                mContext.getResources().getString(R.string.subscriptions_playlist_name),
                Integer.toString(SUBSCRIPTIONS_ID));
        playlist.setChannelKey(SUBS_CHANNEL_ID);
        playlist.setProgramsKey(SUBS_PROGRAMS_IDS);
        playlist.setPlaylistUrl(SUBSCRIPTIONS_URL);
        playlist.setLogoResId(R.drawable.generic_channels);

        if (mediaItems != null && !mediaItems.isEmpty()) {
            if (mediaItems.size() < 20) {
                mediaItems.addAll(service.continueTab(subscriptions).getMediaItems());
                mediaItems.addAll(service.continueTab(subscriptions).getMediaItems());
                mediaItems.addAll(service.continueTab(subscriptions).getMediaItems());
            }

            List<Clip> clips = convertToClips(mediaItems);
            playlist.setClips(clips);
        }

        return playlist;
    }

    public Playlist getHistoryPlaylist() {
        YouTubeMediaServiceSigned service = YouTubeMediaServiceSigned.instance();
        MediaTab history = service.getHistory();
        List<MediaItem> mediaItems = history.getMediaItems();

        Playlist playlist = new Playlist(
                mContext.getResources().getString(R.string.history_playlist_name),
                Integer.toString(HISTORY_ID));
        playlist.setChannelKey(HISTORY_CHANNEL_ID);
        playlist.setProgramsKey(HISTORY_PROGRAMS_IDS);
        playlist.setPlaylistUrl(HISTORY_URL);
        playlist.setLogoResId(R.drawable.generic_channels);

        if (mediaItems != null && !mediaItems.isEmpty()) {
            if (mediaItems.size() < 20) {
                mediaItems.addAll(service.continueTab(history).getMediaItems());
                mediaItems.addAll(service.continueTab(history).getMediaItems());
                mediaItems.addAll(service.continueTab(history).getMediaItems());
            }

            List<Clip> clips = convertToClips(mediaItems);
            playlist.setClips(clips);
        }

        return playlist;
    }

    public Playlist getRecommendedPlaylist() {
        YouTubeMediaServiceSigned service = YouTubeMediaServiceSigned.instance();
        MediaTab recommended = service.getRecommended();
        List<MediaItem> mediaItems = recommended.getMediaItems();

        Playlist playlist = new Playlist(
                mContext.getResources().getString(R.string.recommended_playlist_name),
                Integer.toString(RECOMMENDED_ID));
        playlist.setChannelKey(RECOMMENDED_CHANNEL_ID);
        playlist.setProgramsKey(RECOMMENDED_PROGRAMS_IDS);
        playlist.setPlaylistUrl(RECOMMENDED_URL);
        playlist.setLogoResId(R.drawable.generic_channels);

        if (mediaItems != null && !mediaItems.isEmpty()) {
            if (mediaItems.size() < 20) {
                mediaItems.addAll(service.continueTab(recommended).getMediaItems());
                mediaItems.addAll(service.continueTab(recommended).getMediaItems());
                mediaItems.addAll(service.continueTab(recommended).getMediaItems());
            }

            List<Clip> clips = convertToClips(mediaItems);
            playlist.setClips(clips);
        }

        return playlist;
    }

    private List<Clip> convertToClips(List<MediaItem> videos) {
        if (videos != null) {
            List<Clip> clips = new ArrayList<>();

            for (MediaItem v : videos) {
                clips.add(new Clip(
                        v.getTitle(),
                        v.getDescription(),
                        v.getBackgroundImageUrl(),
                        v.getCardImageUrl(),
                        v.getVideoUrl(),
                        null,
                        false,
                        null,
                        Integer.toString(v.getId()),
                        null,
                        TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_16_9));
            }

            return clips;
        }

        return null;
    }
}
