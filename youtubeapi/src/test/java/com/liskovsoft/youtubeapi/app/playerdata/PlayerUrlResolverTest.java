package com.liskovsoft.youtubeapi.app.playerdata;

import com.liskovsoft.youtubeapi.common.helpers.AppClient;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerUrlResolverTest {
    private static final String TV_PLAYER =
            "https://www.youtube.com/s/player/e937390a/tv-player-ias.vflset/tv-player-ias.js";
    private static final String MAIN_PLAYER =
            "https://www.youtube.com/s/player/e937390a/player_ias.vflset/en_US/base.js";

    @Test
    public void webClientUsesMainPlayerForNChallenge() {
        assertEquals(MAIN_PLAYER, PlayerUrlResolver.resolve(TV_PLAYER, AppClient.WEB_EMBED));
    }

    @Test
    public void tvClientKeepsTvPlayerForNChallenge() {
        assertEquals(TV_PLAYER, PlayerUrlResolver.resolve(TV_PLAYER, AppClient.TV));
    }

    @Test
    public void mainPlayerIsNotRewritten() {
        assertEquals(MAIN_PLAYER, PlayerUrlResolver.resolve(MAIN_PLAYER, AppClient.WEB));
    }

    @Test
    public void es6TvVariantAlsoMapsToV8CompatibleMainPlayer() {
        String es6TvPlayer =
                "https://www.youtube.com/s/player/e937390a/tv-player-es6-tcl.vflset/tv-player-es6-tcl.js";

        assertEquals(MAIN_PLAYER, PlayerUrlResolver.resolve(es6TvPlayer, AppClient.WEB_SAFARI));
    }
}