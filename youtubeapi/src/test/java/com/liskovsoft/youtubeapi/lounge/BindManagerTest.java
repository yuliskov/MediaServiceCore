package com.liskovsoft.youtubeapi.lounge;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.lounge.models.BindData;
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
    private BindManager mBindManager;
    private ScreenManager mScreenManager;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mBindManager = RetrofitHelper.withRegExp(BindManager.class);
        mScreenManager = RetrofitHelper.withJsonPath(ScreenManager.class);
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
        Screen screen = getScreen();

        Call<BindData> bindDataWrapper = mBindManager.bind(SCREEN_NAME, screen.getLoungeToken(), 0);

        BindData bindData = RetrofitHelper.get(bindDataWrapper);

        assertNotNull("Contains bind data", bindData.getBindData());
    }

    private Screen getScreen() {
        Call<ScreenId> screenIdWrapper = mBindManager.createScreenId();
        ScreenId screenId = RetrofitHelper.get(screenIdWrapper);

        Call<ScreenInfos> screenInfosWrapper = mScreenManager.getScreenInfos(screenId.getScreenId());
        ScreenInfos screenInfos = RetrofitHelper.get(screenInfosWrapper);

        return screenInfos.getScreens().get(0);
    }
}