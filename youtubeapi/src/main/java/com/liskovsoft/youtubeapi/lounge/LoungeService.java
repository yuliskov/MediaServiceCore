package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.sharedutils.helpers.AppInfoHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.lounge.models.PairingCode;
import com.liskovsoft.youtubeapi.lounge.models.ScreenId;
import com.liskovsoft.youtubeapi.lounge.models.StateResult;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandItem;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandList;
import com.liskovsoft.youtubeapi.lounge.models.commands.PlaylistParams;
import com.liskovsoft.youtubeapi.lounge.models.info.ScreenItem;
import com.liskovsoft.youtubeapi.lounge.models.info.ScreenList;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import retrofit2.Call;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class LoungeService {
    private static final String TAG = LoungeService.class.getSimpleName();
    private static LoungeService sInstance;
    private final BindManager mBindManager;
    private final InfoManager mScreenManager;
    private final CommandManager mCommandManager;
    private final JsonPathTypeAdapter<CommandList> mLineSkipAdapter;
    private String mScreenName;
    private String mLoungeToken;
    private String mScreenId;
    private String mSessionId;
    private String mGSessionId;
    private String mCtt;
    private String mPlaylistIndex;
    private String mPlaylistId;

    public LoungeService() {
        mBindManager = RetrofitHelper.withRegExp(BindManager.class);
        mScreenManager = RetrofitHelper.withJsonPath(InfoManager.class);
        mCommandManager = RetrofitHelper.withJsonPathSkip(CommandManager.class);
        mLineSkipAdapter = RetrofitHelper.adaptJsonPathSkip(CommandList.class);
    }

    public static LoungeService instance() {
        if (sInstance == null) {
            sInstance = new LoungeService();
        }

        return sInstance;
    }

    public String getPairingCode() {
        initConstants();

        return getPairingCodeInt();
    }

    private String getPairingCodeInt() {
        if (mLoungeToken == null || mScreenId == null) {
            return null;
        }

        Call<PairingCode> pairingCodeWrapper = mBindManager.getPairingCode(
                BindParams.ACCESS_TYPE,
                BindParams.APP,
                mLoungeToken,
                mScreenId,
                mScreenName);
        PairingCode pairingCode = RetrofitHelper.get(pairingCodeWrapper);

        // Pairing code XXX-XXX-XXX-XXX
        return pairingCode != null ? pairingCode.getPairingCode() : null;
    }

    /**
     * Process couldn't be stopped, only interrupted.
     */
    public void startListening(OnCommand callback) {
        // It's common to stream to be interrupted multiple times
        while (true) {
            try {
                initConstants();
                startListeningInt(callback);
                Thread.sleep(3_000); // fix too frequent request
            } catch (InterruptedIOException | NullPointerException e) {
                Log.e(TAG, "We're done. Seems that user has been closed remote session.");
                break;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                // Continue to listen whichever is happening.
            }
        }
    }

    private void initConstants() {
        if (mScreenName == null) {
            mScreenName = String.format(
                    "%s (%s)",
                    AppInfoHelpers.getAppLabel(GlobalPreferences.sInstance.getContext()),
                    Helpers.getUserDeviceName()
            );
        }

        if (mLoungeToken == null) {
            ScreenItem screen = getScreen();

            if (screen != null) {
                mLoungeToken = screen.getLoungeToken();
                mScreenId = screen.getScreenId();
            }
        }
    }

    private void startListeningInt(OnCommand callback) throws IOException {
        Log.d(TAG, "Opening session...");

        CommandList sessionInfos = getSessionBind();

        if (sessionInfos == null) {
            Log.e(TAG, "Can't open a session because it's empty. Expired lounge token or too frequent request?");
            mLoungeToken = null;
            return;
        }

        mSessionId = sessionInfos.getParam(CommandItem.TYPE_SESSION_ID);
        mGSessionId = sessionInfos.getParam(CommandItem.TYPE_G_SESSION_ID);

        Log.d(TAG, "SID: %s, gsessionid: %s", mSessionId, mGSessionId);

        String url = BindParams.createBindRpcUrl(
                mScreenName,
                mLoungeToken,
                mSessionId,
                mGSessionId);
        Request request = new Builder().url(url).build();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // Read infinitely
        builder.readTimeout(0, TimeUnit.MILLISECONDS);

        OkHttpClient client = builder.build();

        Log.d(TAG, "Starting read session...");

        Response response = client.newCall(request).execute();

        InputStream in = response.body().byteStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String result = "";
        String line = "";

        // Skip initial commands: TYPE_SESSION_ID, TYPE_G_SESSION_ID, TYPE_LOUNGE_STATUS, TYPE_GET_NOW_PLAYING
        //processCommands(sessionInfos, callback);

        while((line = reader.readLine()) != null) {
            if (mLoungeToken == null) {
                // restart service
                break;
            }

            result += line + "\n";

            if (line.equals("]") && !result.endsWith("\"noop\"]\n]\n")) {
                Log.d(TAG, "New command: \n" + result);

                CommandList infos = toCommandInfos(result);

                processCommands(infos, callback);

                result = "";
            }
        }

        Log.d(TAG, "Closing session...");

        response.body().close();
    }

    private void processCommands(CommandList commandList, OnCommand callback) {
        for (CommandItem commandItem : commandList.getCommands()) {
            updateData(commandItem);
            callback.onCommand(commandItem);
        }
    }

    public void postStartPlaying(String videoId, long positionMs, long durationMs, boolean isPlaying) {
        postNowPlaying(videoId, positionMs, durationMs, mCtt, mPlaylistId, mPlaylistIndex);
        postStateChange(positionMs, durationMs, isPlaying);
    }

    public void postStateChange(long positionMs, long durationMs, boolean isPlaying) {
        // Live stream fix (negative position)
        if (positionMs < 0) {
            positionMs = Math.abs(positionMs);
        }

        if (durationMs > 0 && positionMs <= durationMs) {
            postOnStateChange(positionMs, durationMs, isPlaying ? CommandParams.STATE_PLAYING : CommandParams.STATE_PAUSED);
        }
    }

    public void resetData() {
        MediaServiceData.instance().setScreenId(null);
        mLoungeToken = null;
    }

    private void postNowPlaying(String videoId, long positionMs, long durationMs, String ctt, String playlistId, String playlistIndex) {
        if (!ServiceHelper.checkNonNull(mSessionId, mGSessionId)) {
            return;
        }

        Log.d(TAG, "Post nowPlaying id: %s, pos: %s, dur: %s...", videoId, positionMs, durationMs);

        Call<Void> wrapper = mCommandManager.postCommand(
                mScreenName, mLoungeToken, mSessionId, mGSessionId,
                CommandParams.getNowPlaying(videoId, positionMs, durationMs, ctt, playlistId, playlistIndex));
        RetrofitHelper.get(wrapper);
    }

    private void postOnStateChange(long positionMs, long durationMs, int state) {
        if (!ServiceHelper.checkNonNull(mSessionId, mGSessionId)) {
            return;
        }

        Log.d(TAG, "Post onStateChange pos: %s, dur: %s...", positionMs, durationMs);

        Call<Void> wrapper = mCommandManager.postCommand(
                mScreenName, mLoungeToken, mSessionId, mGSessionId,
                CommandParams.getOnStateChange(positionMs, durationMs, state));
        RetrofitHelper.get(wrapper);
    }

    private void postOnPrevNextChange() {
        if (!ServiceHelper.checkNonNull(mSessionId, mGSessionId)) {
            return;
        }

        Log.d(TAG, "Post onPrevNextChange...");

        Call<Void> wrapper = mCommandManager.postCommand(
                mScreenName, mLoungeToken, mSessionId, mGSessionId,
                CommandParams.getOnPrevNextChange());
        RetrofitHelper.get(wrapper);
    }

    private void updateData(CommandItem info) {
        if (info != null && info.getPlaylistParams() != null) {
            PlaylistParams playlistData = info.getPlaylistParams();
            mCtt = playlistData.getCtt() != null ? playlistData.getCtt() : mCtt;
            mPlaylistIndex = playlistData.getPlaylistIndex() != null ? playlistData.getPlaylistIndex() : mPlaylistIndex;
            mPlaylistId = playlistData.getPlaylistId() != null ? playlistData.getPlaylistId() : mPlaylistId;
        }
    }

    private ScreenItem getScreen() {
        ScreenItem screenItem = null;
        String screenId = MediaServiceData.instance().getScreenId();

        if (screenId == null) {
            Call<ScreenId> screenIdWrapper = mBindManager.createScreenId();
            ScreenId screenIdContainer = RetrofitHelper.get(screenIdWrapper);
            if (screenIdContainer != null) {
                screenId = screenIdContainer.getScreenId();
                MediaServiceData.instance().setScreenId(screenId);
            }
        }

        if (screenId != null) {
            Call<ScreenList> screenInfosWrapper = mScreenManager.getScreenInfo(screenId);
            ScreenList screenInfos = RetrofitHelper.get(screenInfosWrapper);

            if (screenInfos != null) {
                screenItem = screenInfos.getScreens().get(0);
            }
        }

        return screenItem;
    }

    private CommandList getSessionBind() {
        Call<CommandList> bindDataWrapper = mCommandManager.getSessionData(mScreenName, mLoungeToken, 0);

        return RetrofitHelper.get(bindDataWrapper);
    }

    private CommandList toCommandInfos(String result) {
        return mLineSkipAdapter.read(new ByteArrayInputStream(result.getBytes(Charset.forName("UTF-8"))));
    }

    public interface OnCommand {
        void onCommand(CommandItem info);
    }
}
