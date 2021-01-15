package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import com.liskovsoft.youtubeapi.common.helpers.AppHelper;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.lounge.models.PairingCode;
import com.liskovsoft.youtubeapi.lounge.models.Screen;
import com.liskovsoft.youtubeapi.lounge.models.ScreenId;
import com.liskovsoft.youtubeapi.lounge.models.ScreenInfos;
import com.liskovsoft.youtubeapi.lounge.models.StateResult;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandInfo;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandInfos;
import com.liskovsoft.youtubeapi.lounge.models.commands.PlaylistData;
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
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class LoungeService {
    private static final String SCREEN_NAME_TMP = "TubeNext";
    private static final String LOUNGE_TOKEN_TMP = "AGdO5p8cH1tKYW3OIVFhSMRfjAjV5OxqYdjCezBGrDAaX7be3bcttKQAVKucSpEcoi8qh6rYs_r04DXQhd0_xEZY69s8W5J7rqEMmeaYwJsSi5VivgnFKv4";
    private static final String TAG = LoungeService.class.getSimpleName();
    private static LoungeService sInstance;
    private final BindManager mBindManager;
    private final ScreenManager mScreenManager;
    private final CommandManager mCommandManager;
    private final JsonPathTypeAdapter<CommandInfos> mLineSkipAdapter;
    private final String mScreenName = SCREEN_NAME_TMP;
    private final String mLoungeToken = LOUNGE_TOKEN_TMP;
    private String mSessionId;
    private String mGSessionId;
    private String mCtt;
    private String mPlaylistIndex;
    private String mPlaylistId;

    public LoungeService() {
        mBindManager = RetrofitHelper.withRegExp(BindManager.class);
        mScreenManager = RetrofitHelper.withJsonPath(ScreenManager.class);
        mCommandManager = RetrofitHelper.withJsonPathSkip(CommandManager.class);
        mLineSkipAdapter = RetrofitHelper.adaptJsonPathSkip(CommandInfos.class);
    }

    public static LoungeService instance() {
        if (sInstance == null) {
            sInstance = new LoungeService();
        }

        return sInstance;
    }

    public String getPairingCode() {
        Screen screen = getScreen();
        Call<PairingCode> pairingCodeWrapper = mBindManager.getPairingCode(
                BindParams.ACCESS_TYPE,
                BindParams.APP,
                screen.getLoungeToken(),
                screen.getScreenId(),
                mScreenName);
        PairingCode pairingCode = RetrofitHelper.get(pairingCodeWrapper);

        // Pairing code XXX-XXX-XXX-XXX
        return pairingCode != null ? pairingCode.getPairingCode() : null;
    }

    public void startListening(OnCommand callback) throws IOException {
        Log.d(TAG, "Opening session...");

        CommandInfos sessionBind = getSessionBind();

        mSessionId = sessionBind.getParam(CommandInfo.TYPE_SESSION_ID);
        mGSessionId = sessionBind.getParam(CommandInfo.TYPE_G_SESSION_ID);

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

        Response response = client.newCall(request).execute();

        InputStream in = response.body().byteStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String result = "";
        String line = "";

        while((line = reader.readLine()) != null) {
            result += line + "\n";

            if (line.equals("]") && !result.endsWith("\"noop\"]\n]\n")) {
                Log.d(TAG, "New command: \n" + result);

                CommandInfos infos = toCommandInfos(result);

                for (CommandInfo info : infos.getCommands()) {
                    updateData(info);
                    callback.onCommand(info);
                }

                result = "";
            }
        }

        Log.d(TAG, "Closing session...");

        mSessionId = null;
        mGSessionId = null;

        response.body().close();
    }

    public void postPlaying(String videoId, long positionMs, long lengthMs) {
        if (!AppHelper.checkNonNull(mSessionId, mGSessionId, mCtt, mPlaylistId, mPlaylistIndex)) {
            return;
        }

        postNowPlaying(videoId, positionMs, lengthMs);
        postOnStateChange(positionMs, lengthMs);
    }

    private void postNowPlaying(String videoId, long positionMs, long lengthMs) {
        Log.d(TAG, "Post nowPlaying...");

        Call<StateResult> wrapper = mCommandManager.postCommand(
                mScreenName, mLoungeToken, mSessionId, mGSessionId,
                CommandParams.getNowPlaying(videoId, positionMs, lengthMs, mCtt, mPlaylistId, mPlaylistIndex));
        RetrofitHelper.get(wrapper);
    }

    private void postOnStateChange(long positionMs, long lengthMs) {
        Log.d(TAG, "Post update...");

        Call<StateResult> wrapper = mCommandManager.postCommand(
                mScreenName, mLoungeToken, mSessionId, mGSessionId,
                CommandParams.getOnStateChange(positionMs, lengthMs));
        RetrofitHelper.get(wrapper);
    }

    private void updateData(CommandInfo info) {
        if (info != null && info.getPlaylistData() != null) {
            PlaylistData playlistData = info.getPlaylistData();
            mCtt = playlistData.getCtt();
            mPlaylistIndex = playlistData.getCurrentIndex();
            mPlaylistId = playlistData.getListId();
        }
    }

    private Screen getScreen() {
        Call<ScreenId> screenIdWrapper = mBindManager.createScreenId();
        ScreenId screenId = RetrofitHelper.get(screenIdWrapper);

        Call<ScreenInfos> screenInfosWrapper = mScreenManager.getScreenInfos(screenId.getScreenId());
        ScreenInfos screenInfos = RetrofitHelper.get(screenInfosWrapper);

        return screenInfos.getScreens().get(0);
    }

    private CommandInfos getSessionBind() {
        Call<CommandInfos> bindDataWrapper = mCommandManager.getSessionBindData(mScreenName, mLoungeToken, 0);

        return RetrofitHelper.get(bindDataWrapper);
    }

    private CommandInfos toCommandInfos(String result) {
        return mLineSkipAdapter.read(new ByteArrayInputStream(result.getBytes(Charset.forName("UTF-8"))));
    }

    public interface OnCommand {
        void onCommand(CommandInfo info);
    }
}
