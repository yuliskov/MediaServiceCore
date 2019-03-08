package com.liskovsoft.youtubeapi.auth;

import com.liskovsoft.youtubeapi.auth.models.AccessToken;
import com.liskovsoft.youtubeapi.auth.models.UserCode;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
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
    private BrowserAuth mService;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withGson(BrowserAuth.class);
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