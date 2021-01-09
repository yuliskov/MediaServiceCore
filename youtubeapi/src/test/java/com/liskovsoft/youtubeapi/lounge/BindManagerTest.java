package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.lounge.models.Command;
import com.liskovsoft.youtubeapi.lounge.models.CommandInfos;
import com.liskovsoft.youtubeapi.lounge.models.ScreenInfos;
import com.liskovsoft.youtubeapi.lounge.models.PairingCode;
import com.liskovsoft.youtubeapi.lounge.models.Screen;
import com.liskovsoft.youtubeapi.lounge.models.ScreenId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class BindManagerTest {
    private static final String SCREEN_NAME = "TubeNext";
    private static final String LOUNGE_TOKEN_TMP = "AGdO5p8cH1tKYW3OIVFhSMRfjAjV5OxqYdjCezBGrDAaX7be3bcttKQAVKucSpEcoi8qh6rYs_r04DXQhd0_xEZY69s8W5J7rqEMmeaYwJsSi5VivgnFKv4";
    private static final String SCREEN_ID_TMP = "910nbko7d2d6qtthu2609a3id6";
    private BindManager mBindManager;
    private ScreenManager mScreenManager;
    private CommandManager mCommandManager;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mBindManager = RetrofitHelper.withRegExp(BindManager.class);
        mScreenManager = RetrofitHelper.withJsonPath(ScreenManager.class);
        mCommandManager = RetrofitHelper.withJsonPathSkip(CommandManager.class);
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

    @Test
    public void testThatSecondBindDataIsNotEmpty() throws InterruptedException {
        CommandInfos firstBind = getFirstBind();
        Command command1 = firstBind.getCommands().get(0);
        Command command2 = firstBind.getCommands().get(1);
        String screenId = command1.getCommandParams().get(0);
        String sessionId = command2.getCommandParams().get(0);

        for (int i = 0; i < 10; i++) {
            Call<CommandInfos> bindDataWrapper = mCommandManager.bind2(SCREEN_NAME, LOUNGE_TOKEN_TMP, screenId, sessionId);

            CommandInfos bindData = RetrofitHelper.get(bindDataWrapper);

            //assertNotNull("Contains bind data", bindData);

            //Thread.sleep(10_000);
        }
    }

    private CommandInfos getFirstBind() {
        Call<CommandInfos> bindDataWrapper = mCommandManager.bind1(SCREEN_NAME, LOUNGE_TOKEN_TMP, 0);

        return RetrofitHelper.get(bindDataWrapper);
    }

    private Screen getScreen() {
        Call<ScreenId> screenIdWrapper = mBindManager.createScreenId();
        ScreenId screenId = RetrofitHelper.get(screenIdWrapper);

        Call<ScreenInfos> screenInfosWrapper = mScreenManager.getScreenInfos(screenId.getScreenId());
        ScreenInfos screenInfos = RetrofitHelper.get(screenInfosWrapper);

        return screenInfos.getScreens().get(0);
    }
}