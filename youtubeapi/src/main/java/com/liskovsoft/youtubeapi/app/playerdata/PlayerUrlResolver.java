package com.liskovsoft.youtubeapi.app.playerdata;

import com.liskovsoft.youtubeapi.common.helpers.AppClient;

public final class PlayerUrlResolver {
    private static final String TV_PLAYER_PATH = "/tv-player-";
    private static final String MAIN_PLAYER_PATH = "/player_ias.vflset/en_US/base.js";

    private PlayerUrlResolver() {
    }

    public static String resolve(String playerUrl, AppClient client) {
        if (playerUrl == null || client == null || client.isTVClient() || !playerUrl.contains(TV_PLAYER_PATH)) {
            return playerUrl;
        }

        int playerVariantStart = playerUrl.indexOf(TV_PLAYER_PATH);
        return playerUrl.substring(0, playerVariantStart) + MAIN_PLAYER_PATH;
    }
}