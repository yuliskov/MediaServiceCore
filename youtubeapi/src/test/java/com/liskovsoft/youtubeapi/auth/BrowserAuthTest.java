package com.liskovsoft.youtubeapi.auth;

import com.liskovsoft.youtubeapi.auth.models.AccessToken;
import com.liskovsoft.youtubeapi.auth.models.UserCode;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
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
public class BrowserAuthTest {
    private static final String RAW_POST_DATA = "client_id=861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com&client_secret=SboVhoG9s0rNafixCSGGKXAT&grant_type=refresh_token&refresh_token=1%2FdXXiG98cBB9lJ9YwGpNmVzboP3X24FUdLcvE1Y0M8QWtTYHpWsakvNjPKuJlk68J";
    private static final String CLIENT_ID = "861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "SboVhoG9s0rNafixCSGGKXAT";
    private static final String GRANT_TYPE = "refresh_token";
    private static final String REFRESH_TOKEN = "1/dXXiG98cBB9lJ9YwGpNmVzboP3X24FUdLcvE1Y0M8QWtTYHpWsakvNjPKuJlk68J";
    private BrowserAuth mService;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withGson(BrowserAuth.class);
    }

    @Test
    public void testThatUserIsAuthenticated() throws IOException {
        Call<AccessToken> wrapper = mService.getAuthToken(RequestBody.create(null, RAW_POST_DATA.getBytes()));

        Response<AccessToken> execute = wrapper.execute();

        AccessToken token = execute.body();

        assertEquals("Auth type Bearer", "Bearer", token.getTokenType());
        assertTrue("Token not null", token.getAccessToken().length() > 50);
    }

    @Test
    public void testThatUserIsAuthenticated2() throws IOException {
        Call<AccessToken> wrapper = mService.getAuthToken2(
                CLIENT_ID,
                CLIENT_SECRET,
                GRANT_TYPE,
                REFRESH_TOKEN);

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
        AccessToken token = getAccessToken();
        assertEquals("authorization_pending", token.getError());
    }

    private boolean notEmpty(String userCode) {
        System.out.println("Important code is: " + userCode);
        return userCode != null && userCode.length() > 5;
    }

    private UserCode getUserCode() throws IOException {
        Call<UserCode> userCode = mService.getUserCode(AuthParams.getClientId(), AuthParams.getAppScope());
        Response<UserCode> response = userCode.execute();
        return response.body();
    }

    private AccessToken getAccessToken() throws IOException {
        UserCode userCode = getUserCode();
        System.out.println("The user code is: " + userCode.getUserCode());

        Call<AccessToken> token = mService.getAuthToken(AuthParams.getClientId(), userCode.getDeviceCode(), AuthParams.getClientSecret(), AuthParams.getGrantType());
        Response<AccessToken> response = token.execute();
        return response.body();
    }
}