package com.liskovsoft.youtubeapi;

import com.liskovsoft.youtubeapi.models.AccessToken;
import com.liskovsoft.youtubeapi.models.UserCode;
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
    private static final String DEFAULT_APP_CLIENT_ID = "861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com";
    private static final String DEFAULT_APP_SCOPE = "http://gdata.youtube.com https://www.googleapis.com/auth/youtube-paid-content";
    private static final String DEFAULT_APP_CLIENT_SECRET = "SboVhoG9s0rNafixCSGGKXAT";
    private static final String DEFAULT_APP_GRANT_TYPE = "http://oauth.net/grant_type/device/1.0";
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
        Call<UserCode> userCode = mService.getUserCode(DEFAULT_APP_CLIENT_ID, DEFAULT_APP_SCOPE);
        Response<UserCode> response = userCode.execute();
        return response.body();
    }

    private AccessToken getAccessToken() throws IOException {
        UserCode userCode = getUserCode();
        System.out.println("The user code is: " + userCode.getUserCode());

        Call<AccessToken> token = mService.getAuthToken(DEFAULT_APP_CLIENT_ID, userCode.getDeviceCode(), DEFAULT_APP_CLIENT_SECRET, DEFAULT_APP_GRANT_TYPE);
        Response<AccessToken> response = token.execute();
        return response.body();
    }
}