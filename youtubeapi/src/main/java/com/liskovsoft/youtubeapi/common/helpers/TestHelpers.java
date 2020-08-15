package com.liskovsoft.youtubeapi.common.helpers;

import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.auth.models.RefreshTokenResult;
import okhttp3.RequestBody;
import retrofit2.Call;

import java.io.IOException;

public class TestHelpers {
    private static String mAuthorization; // type: Bearer
    private static final String RAW_POST_DATA = "client_id=861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com&client_secret=SboVhoG9s0rNafixCSGGKXAT&refresh_token=1%2F%2F0ca0zVzDYAcWCCgYIARAAGAwSNwF-L9IrCkqjDqPyup8sXFA40LiTGh-8yW2jM4lLBOXyhcRa07fDM35jM-dU80PUemu1u1F8-AY&grant_type=refresh_token";

    // Mafia: Definitive Edition - Official Story Trailer | Summer of Gaming 2020
    public static final String VIDEO_ID_SIMPLE = "s2lGEhSlOTY";

    // LINDEMANN - Mathematik ft. Haftbefehl (Official Video)
    public static final String VIDEO_ID_CIPHERED = "0YEZiDtnbdA";

    // NEWS ONE
    public static final String VIDEO_ID_LIVE = "3e0FsU1N6OQ";

    public static String getAuthorization() throws IOException {
        if (mAuthorization != null) {
            return mAuthorization;
        }

        AuthManager authService = RetrofitHelper.withGson(AuthManager.class);
        Call<RefreshTokenResult> wrapper = authService.getRefreshToken(RequestBody.create(null, RAW_POST_DATA.getBytes()));
        RefreshTokenResult token = wrapper.execute().body();

        if (token == null) {
            throw new IllegalStateException("Token is null");
        }

        if (token.getAccessToken() == null) {
            throw new IllegalStateException("Authorization is null");
        }

        mAuthorization = String.format("%s %s", token.getTokenType(), token.getAccessToken());

        return mAuthorization;
    }
}
