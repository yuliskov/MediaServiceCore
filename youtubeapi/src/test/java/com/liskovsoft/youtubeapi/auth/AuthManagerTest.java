package com.liskovsoft.youtubeapi.auth;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.auth.models.RefreshToken;
import com.liskovsoft.youtubeapi.auth.models.AccessToken;
import com.liskovsoft.youtubeapi.auth.models.UserCode;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import okhttp3.RequestBody;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class AuthManagerTest {
    private static final String RAW_POST_DATA = "client_id=861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com&client_secret=SboVhoG9s0rNafixCSGGKXAT&grant_type=refresh_token&refresh_token=1%2FdXXiG98cBB9lJ9YwGpNmVzboP3X24FUdLcvE1Y0M8QWtTYHpWsakvNjPKuJlk68J";
    private static final String CLIENT_ID = "861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "SboVhoG9s0rNafixCSGGKXAT";
    private static final String GRANT_TYPE = "refresh_token";
    private static final String REFRESH_TOKEN = "1//0ca0zVzDYAcWCCgYIARAAGAwSNwF-L9IrCkqjDqPyup8sXFA40LiTGh-8yW2jM4lLBOXyhcRa07fDM35jM-dU80PUemu1u1F8-AY";
    private AuthManager mService;
    private AppService mAppService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withGson(AuthManager.class);
        mAppService = AppService.instance();
    }

    // Fail
    //@Test
    public void testThatUserIsAuthenticated() throws IOException {
        Call<AccessToken> wrapper = mService.getAccessToken(RequestBody.create(null, RAW_POST_DATA.getBytes()));

        Response<AccessToken> execute = wrapper.execute();

        AccessToken token = execute.body();

        assertEquals("Auth type Bearer", "Bearer", token.getTokenType());
        assertTrue("Token not null", token.getAccessToken().length() > 50);
    }
    
    @Test
    public void testThatUserCanRefreshToken() throws IOException {
        Call<AccessToken> wrapper = mService.getAccessToken(REFRESH_TOKEN, CLIENT_ID,
                CLIENT_SECRET,
                GRANT_TYPE);

        Response<AccessToken> execute = wrapper.execute();

        AccessToken token = execute.body();

        assertEquals("Auth type Bearer", "Bearer", token.getTokenType());
        assertTrue("Token not null", token.getAccessToken().length() > 50);
    }

    @Test
    public void testThatUserCodeFieldsNotEmpty() throws IOException {
        UserCode code = getUserCode();
        assertTrue(notEmpty(code.getUserCode()));
        assertTrue(notEmpty(code.getDeviceCode()));
    }

    @Test
    public void testThatUserStillNotSignedIn() throws IOException {
        RefreshToken token = getAccessToken();
        assertEquals("authorization_pending", token.getError());
    }

    private boolean notEmpty(String userCode) {
        System.out.println("Important code is: " + userCode);
        return userCode != null && userCode.length() > 5;
    }

    private UserCode getUserCode() throws IOException {
        Call<UserCode> userCode = mService.getUserCode(mAppService.getClientId(), AuthParams.getAppScope());
        Response<UserCode> response = userCode.execute();
        return response.body();
    }

    private RefreshToken getAccessToken() throws IOException {
        UserCode userCode = getUserCode();
        System.out.println("The user code is: " + userCode.getUserCode());

        Call<RefreshToken> token = mService.getRefreshToken(userCode.getDeviceCode(), mAppService.getClientId(), mAppService.getClientSecret(), AuthParams.getAccessGrantType());
        Response<RefreshToken> response = token.execute();
        return response.body();
    }
}