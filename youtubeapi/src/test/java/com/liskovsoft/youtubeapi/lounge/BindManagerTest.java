package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.sharedutils.okhttp.OkHttpManager;
import com.liskovsoft.googlecommon.common.helpers.DefaultHeaders;
import com.liskovsoft.googlecommon.common.converters.jsonpath.typeadapter.JsonPathTypeAdapter;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.lounge.models.bind.PairingCode;
import com.liskovsoft.youtubeapi.lounge.models.bind.ScreenId;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandItem;
import com.liskovsoft.youtubeapi.lounge.models.commands.CommandList;
import com.liskovsoft.youtubeapi.lounge.models.info.TokenInfo;
import com.liskovsoft.youtubeapi.lounge.models.info.TokenInfoList;
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
    private static final String DEVICE_ID = "2a026ce9-4429-4c5e-8ef5-0101eddf5671"; // Should be random UUID
    private BindManager mBindManager;
    private InfoManager mInfoManager;
    private CommandManager mCommandManager;
    private JsonPathTypeAdapter<CommandList> mAdapter;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mBindManager = RetrofitHelper.create(BindManager.class);
        mInfoManager = RetrofitHelper.create(InfoManager.class);
        mCommandManager = RetrofitHelper.create(CommandManager.class);
        mAdapter = RetrofitHelper.adaptJsonPathSkip(CommandList.class);
    }

    @Test
    public void testThatPairingCodeGeneratedSuccessfully() {
        TokenInfo screen = getScreen();
        Call<PairingCode> pairingCodeWrapper = mBindManager.getPairingCode(screen.getLoungeToken(),
                screen.getScreenId(), SCREEN_NAME, BindParams.ACCESS_TYPE, BindParams.APP);
        PairingCode pairingCode = RetrofitHelper.get(pairingCodeWrapper);

        // Pairing code XXX-XXX-XXX-XXX
        assertNotNull("Pairing code not empty", pairingCode.getPairingCode());
    }

    @Test
    public void testThatFirstBindDataIsNotEmpty() {
        TokenInfo screen = getScreen();

        CommandList bindData = getFirstBind(screen.getLoungeToken());

        assertNotNull("Contains bind data", bindData);
    }

    @Ignore("Expired token")
    @Test
    public void testThatRequestInSuccessfulRaw() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", DefaultHeaders.APP_USER_AGENT);

        Response response = OkHttpManager.instance().doPostRequest("https://www.youtube.com/api/lounge/bc/bind?device=LOUNGE_SCREEN&theme=cl" +
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
        TokenInfo screen = getScreen();

        CommandList firstBind = getFirstBind(screen.getLoungeToken());

        String sessionId = firstBind.getParam(CommandItem.TYPE_SESSION_ID);
        String gSessionId = firstBind.getParam(CommandItem.TYPE_G_SESSION_ID);

        String url = BindParams.createBindRpcUrl(
                SCREEN_NAME,
                DEVICE_ID,
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
        Call<CommandList> bindDataWrapper = mCommandManager.getSessionData(SCREEN_NAME, DEVICE_ID, loungeToken, 0);

        return RetrofitHelper.get(bindDataWrapper);
    }

    private TokenInfo getScreen() {
        Call<ScreenId> screenIdWrapper = mBindManager.createScreenId();
        ScreenId screenId = RetrofitHelper.get(screenIdWrapper);

        Call<TokenInfoList> tokenInfoListWrapper = mInfoManager.getTokenInfo(screenId.getScreenId());
        TokenInfoList tokenInfoList = RetrofitHelper.get(tokenInfoListWrapper);

        return tokenInfoList.getTokenInfos().get(0);
    }

    private CommandList toObject(String result) {
        return mAdapter.read(new ByteArrayInputStream(result.getBytes(Charset.forName("UTF-8"))));
    }
}