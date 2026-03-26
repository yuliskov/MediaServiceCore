package com.liskovsoft.googleapi.oauth2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.liskovsoft.googlecommon.common.constants.ConstantsService;
import com.liskovsoft.googlecommon.common.constants.data.ConstantsResult;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.ApiKeys;
import com.liskovsoft.googlecommon.common.models.auth.AccessToken;
import com.liskovsoft.googlecommon.common.models.auth.UserCode;
import com.liskovsoft.sharedutils.helpers.Helpers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

@RunWith(RobolectricTestRunner.class)
public class OAuth2ApiTest {
    private OAuth2Api mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.create(OAuth2Api.class);

        RetrofitOkHttpHelper.setDisableCompression(true);
        RetrofitOkHttpHelper.getAuthHeaders().clear();
    }
    
    @Test
    public void testThatUserCanUpdateAccessToken() {
        Call<AccessToken> wrapper = mService.updateAccessToken(getClientId(), getClientSecret(), OAuth2ApiHelper.GRANT_TYPE_REFRESH, ApiKeys.REFRESH_TOKEN);

        AccessToken token = RetrofitHelper.getWithErrors(wrapper);

        assertEquals("Auth type Bearer", "Bearer", token.getTokenType());
        assertTrue("Token not null", token.getAccessToken().length() > 50);
    }

    @Test
    public void testThatUserCodeFieldsNotEmpty() throws IOException {
        UserCode code = getUserCode();
        assertTrue(notEmpty(code.getUserCode()));
        assertTrue(notEmpty(code.getDeviceCode()));
        assertTrue(notEmpty(code.getVerificationUrl()));
        assertTrue(code.getInterval() > 0);
        assertTrue(code.getExpiresIn() > 0);
    }

    @Test
    public void testThatUserStillNotSignedIn() throws IOException {
        try {
            AccessToken token = getAccessToken();
            assertTrue("This part shouldn't be executed", true);
        } catch (IllegalStateException e) {
            assertTrue("Waiting till the user enter the code...", Helpers.contains(e.getMessage(), "authorization_pending"));
        }
    }

    //@Test
    //public void testThatAccountsListNotEmpty() {
    //    Call<AccountsList> wrapper = mService.getAccountsList(OAuth2ApiHelper.getAccountsListQuery());
    //
    //    AccountsList accountsList = RetrofitHelper.get(wrapper);
    //
    //    AccountInt firstAccount = accountsList.getAccounts().get(0);
    //
    //    assertNotNull("Contains Name", firstAccount.getName());
    //    assertNotNull("Contains Thumbnails", firstAccount.getThumbnails());
    //    assertTrue("Is selected", firstAccount.isSelected());
    //}

    private boolean notEmpty(String userCode) {
        System.out.println("Important code is: " + userCode);
        return userCode != null && userCode.length() > 5;
    }

    private UserCode getUserCode() throws IOException {
        Call<UserCode> userCode = mService.getUserCode(getClientId(), OAuth2ApiHelper.DRIVE_SCOPE);
        Response<UserCode> response = userCode.execute();
        return response.body();
    }

    private AccessToken getAccessToken() throws IOException {
        UserCode userCode = getUserCode();
        System.out.println("The user code is: " + userCode.getUserCode());

        Call<AccessToken> token = mService.getAccessToken(getClientId(), getClientSecret(), userCode.getDeviceCode(), OAuth2ApiHelper.GRANT_TYPE);
        return RetrofitHelper.getWithErrors(token);
    }

    private String getClientId() {
        ConstantsResult constants = ConstantsService.getConstants();
        return constants != null ? constants.getClientId() : null;
    }

    private String getClientSecret() {
        ConstantsResult constants = ConstantsService.getConstants();
        return constants != null ? constants.getClientSecret() : null;
    }
}