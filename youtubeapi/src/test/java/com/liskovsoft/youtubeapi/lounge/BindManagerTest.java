package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandInfo;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandInfos;
import com.liskovsoft.youtubeapi.lounge.models.PairingCode;
import com.liskovsoft.youtubeapi.lounge.models.Screen;
import com.liskovsoft.youtubeapi.lounge.models.ScreenId;
import com.liskovsoft.youtubeapi.lounge.models.ScreenInfos;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class BindManagerTest {
    private static final String SCREEN_NAME = "TubeNext";
    private static final String LOUNGE_TOKEN_TMP = "AGdO5p8cH1tKYW3OIVFhSMRfjAjV5OxqYdjCezBGrDAaX7be3bcttKQAVKucSpEcoi8qh6rYs_r04DXQhd0_xEZY69s8W5J7rqEMmeaYwJsSi5VivgnFKv4";
    private static final String SCREEN_ID_TMP = "910nbko7d2d6qtthu2609a3id6";
    private BindManager mBindManager;
    private ScreenManager mScreenManager;
    private CommandManager mCommandManager;
    private JsonPathTypeAdapter<CommandInfos> mAdapter;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mBindManager = RetrofitHelper.withRegExp(BindManager.class);
        mScreenManager = RetrofitHelper.withJsonPath(ScreenManager.class);
        mCommandManager = RetrofitHelper.withJsonPathSkip(CommandManager.class);
        mAdapter = RetrofitHelper.adaptJsonPathSkip(CommandInfos.class);
    }

    @Test
    public void testThatPairingCodeGeneratedSuccessfully() {
        Screen screen = getScreen();
        Call<PairingCode> pairingCodeWrapper = mBindManager.getPairingCode(BindManagerParams.ACCESS_TYPE, BindManagerParams.APP, screen.getLoungeToken(),
                screen.getScreenId(), SCREEN_NAME);
        PairingCode pairingCode = RetrofitHelper.get(pairingCodeWrapper);

        // Pairing code XXX-XXX-XXX-XXX
        assertNotNull("Pairing code not empty", pairingCode.getPairingCode());
    }

    @Test
    public void testThatFirstBindDataIsNotEmpty() {
        CommandInfos bindData = getFirstBind();

        assertNotNull("Contains bind data", bindData);
    }

    //@Ignore("Long running test")
    @Test
    public void testBindStream() throws IOException {
        CommandInfos firstBind = getFirstBind();

        String sessionId = firstBind.getParam(CommandInfo.TYPE_SESSION_ID);
        String gSessionId = firstBind.getParam(CommandInfo.TYPE_G_SESSION_ID);

        String url = BindManagerParams.createBindRpcUrl(
                SCREEN_NAME,
                LOUNGE_TOKEN_TMP,
                sessionId,
                gSessionId);
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
                System.out.println("New chunk: \n" + result);
                CommandInfos commandInfos = toObject(result);
                result = "";
            }
        }

        response.body().close();
    }

    private CommandInfos getFirstBind() {
        Call<CommandInfos> bindDataWrapper = mCommandManager.getFistBindData(SCREEN_NAME, LOUNGE_TOKEN_TMP, 0);

        return RetrofitHelper.get(bindDataWrapper);
    }

    private Screen getScreen() {
        Call<ScreenId> screenIdWrapper = mBindManager.createScreenId();
        ScreenId screenId = RetrofitHelper.get(screenIdWrapper);

        Call<ScreenInfos> screenInfosWrapper = mScreenManager.getScreenInfos(screenId.getScreenId());
        ScreenInfos screenInfos = RetrofitHelper.get(screenInfosWrapper);

        return screenInfos.getScreens().get(0);
    }

    private CommandInfos toObject(String result) {
        return mAdapter.read(new ByteArrayInputStream(result.getBytes(Charset.forName("UTF-8"))));
    }
}