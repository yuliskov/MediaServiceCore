package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.sharedutils.okhttp.OkHttpHelpers;
import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandItem;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandList;
import com.liskovsoft.youtubeapi.lounge.models.PairingCode;
import com.liskovsoft.youtubeapi.lounge.models.info.ScreenItem;
import com.liskovsoft.youtubeapi.lounge.models.ScreenId;
import com.liskovsoft.youtubeapi.lounge.models.info.ScreenList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Ignore;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class BindManagerTest {
    private static final String SCREEN_NAME = "TubeNext";
    private BindManager mBindManager;
    private InfoManager mScreenManager;
    private CommandManager mCommandManager;
    private JsonPathTypeAdapter<CommandList> mAdapter;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mBindManager = RetrofitHelper.withRegExp(BindManager.class);
        mScreenManager = RetrofitHelper.withJsonPath(InfoManager.class);
        mCommandManager = RetrofitHelper.withJsonPathSkip(CommandManager.class);
        mAdapter = RetrofitHelper.adaptJsonPathSkip(CommandList.class);
    }

    @Test
    public void testThatPairingCodeGeneratedSuccessfully() {
        ScreenItem screen = getScreen();
        Call<PairingCode> pairingCodeWrapper = mBindManager.getPairingCode(BindParams.ACCESS_TYPE, BindParams.APP, screen.getLoungeToken(),
                screen.getScreenId(), SCREEN_NAME);
        PairingCode pairingCode = RetrofitHelper.get(pairingCodeWrapper);

        // Pairing code XXX-XXX-XXX-XXX
        assertNotNull("Pairing code not empty", pairingCode.getPairingCode());
    }

    @Test
    public void testThatFirstBindDataIsNotEmpty() {
        ScreenItem screen = getScreen();

        CommandList bindData = getFirstBind(screen.getLoungeToken());

        assertNotNull("Contains bind data", bindData);
    }

    @Ignore("Expired token")
    @Test
    public void testThatRequestInSuccessfulRaw() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", AppConstants.APP_USER_AGENT);

        Response response = OkHttpHelpers.doPostOkHttpRequest("https://www.youtube.com/api/lounge/bc/bind?device=LOUNGE_SCREEN&theme=cl" +
                        "&capabilities=dsp%2Cmic%2Cdpa" + "&mdxVersion=2&VER=8&v=2&t=1&app=lb-v4&id=2a026ce9-4429-4c5e-8ef5-0101eddf5671&AID=42&zx" +
                        "=xxxxxxxxxxxx&RID=1337&name=SmartTubeNext" + "%20on%20Yuriy%27s%20Fire%20TV&loungeIdToken=AGdO5p" +
                        "-IRMDTZHHW3EtJN26YLqcM_a1UqaC8WwXGENvetjadXhf0a3tiTZqSUEO5SzNbft" + "-ASL5gG9oW7jKspjLpMD5nFTcKXIWtTyHU4HtLioA9COHHVwE", headers,
                "count=0", "application/x-www-form-urlencoded");

        String content = response.body().string();
        assertNotNull("Contains bind data", content);
    }

    @Ignore("Long running test")
    @Test
    public void testBindStream() throws IOException {
        ScreenItem screen = getScreen();

        CommandList firstBind = getFirstBind(screen.getLoungeToken());

        String sessionId = firstBind.getParam(CommandItem.TYPE_SESSION_ID);
        String gSessionId = firstBind.getParam(CommandItem.TYPE_G_SESSION_ID);

        String url = BindParams.createBindRpcUrl(
                SCREEN_NAME,
                screen.getLoungeToken(),
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
                CommandList commandInfos = toObject(result);
                result = "";
            }
        }

        response.body().close();
    }

    private CommandList getFirstBind(String loungeToken) {
        Call<CommandList> bindDataWrapper = mCommandManager.getSessionData(SCREEN_NAME, loungeToken, 0);

        return RetrofitHelper.get(bindDataWrapper);
    }

    private ScreenItem getScreen() {
        Call<ScreenId> screenIdWrapper = mBindManager.createScreenId();
        ScreenId screenId = RetrofitHelper.get(screenIdWrapper);

        Call<ScreenList> screenInfosWrapper = mScreenManager.getScreenInfo(screenId.getScreenId());
        ScreenList screenInfos = RetrofitHelper.get(screenInfosWrapper);

        return screenInfos.getScreens().get(0);
    }

    private CommandList toObject(String result) {
        return mAdapter.read(new ByteArrayInputStream(result.getBytes(Charset.forName("UTF-8"))));
    }
}