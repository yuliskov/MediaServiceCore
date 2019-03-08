package com.liskovsoft.youtubeapi.auth;

import com.liskovsoft.youtubeapi.auth.models.AccessToken;
import com.liskovsoft.youtubeapi.auth.models.UserCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BrowserAuthTest {
    private BrowserAuth mService;

    @Before
    public void setUp() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.youtube.com") // ignored in case of full url
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(BrowserAuth.class);
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
        Call<UserCode> userCode = mService.getUserCode(AuthHelper.getClientId(), AuthHelper.getAppScope());
        Response<UserCode> response = userCode.execute();
        return response.body();
    }

    private AccessToken getAccessToken() throws IOException {
        UserCode userCode = getUserCode();
        System.out.println("The user code is: " + userCode.getUserCode());

        Call<AccessToken> token = mService.getAuthToken(AuthHelper.getClientId(), userCode.getDeviceCode(), AuthHelper.getClientSecret(), AuthHelper.getGrantType());
        Response<AccessToken> response = token.execute();
        return response.body();
    }
}