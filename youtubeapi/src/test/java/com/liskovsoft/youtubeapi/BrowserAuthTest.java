package com.liskovsoft.youtubeapi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BrowserAuthTest {
    private static final String DEFAULT_APP_CLIENT_ID = "861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com";
    private static final String DEFAULT_APP_SCOPE = "http://gdata.youtube.com https://www.googleapis.com/auth/youtube-paid-content";
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
        Call<UserCode> userCode = mService.getUserCode(DEFAULT_APP_CLIENT_ID, DEFAULT_APP_SCOPE);
        Response<UserCode> response = userCode.execute();
        UserCode code = response.body();
        System.out.println("UserCode: " + code.getUserCode());
        System.out.println("DeviceCode: " + code.getDeviceCode());
        assertTrue(code.getUserCode() != null && code.getUserCode().length() > 5);
        assertTrue(code.getDeviceCode() != null && code.getDeviceCode().length() > 5);
    }
}