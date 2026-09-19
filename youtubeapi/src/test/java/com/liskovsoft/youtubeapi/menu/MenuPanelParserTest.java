package com.liskovsoft.youtubeapi.menu;

import com.google.gson.*;
import org.junit.Test;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class MenuPanelParserTest {
    private JsonObject fixture(String name) {
        return new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream("/context-menu/" + name + ".json"), StandardCharsets.UTF_8)).getAsJsonObject();
    }
    private JsonArray rows(JsonObject f) {
        return f.getAsJsonObject("content").getAsJsonObject("engagementPanelSectionListRenderer")
                .getAsJsonObject("content").getAsJsonObject("listViewModel").getAsJsonArray("listItems");
    }
    private JsonObject command(JsonElement row) {
        return row.getAsJsonObject().getAsJsonObject("listItemViewModel").getAsJsonObject("rendererContext")
                .getAsJsonObject("commandContext").getAsJsonObject("onTap").getAsJsonObject("innertubeCommand");
    }
    @Test public void readsDistinctCommandsFromActualVideoAndBrowseVideoResponses() {
        for (String name : new String[]{"video", "browse-video"}) {
            MenuPanelParser.Result result = MenuPanelParser.parse(fixture(name));
            assertNotNull(result.videoId); assertNotNull(result.channelId);
            assertNotNull(result.notInterestedToken); assertNotNull(result.notRecommendChannelToken);
            assertNotEquals(result.notInterestedToken, result.notRecommendChannelToken);
        }
    }
    @Test public void channelMenuDoesNotAcquireVideoActions() {
        MenuPanelParser.Result result = MenuPanelParser.parse(fixture("channel"));
        assertNotNull(result.channelId); assertNull(result.videoId);
        assertNull(result.notInterestedToken); assertNull(result.notRecommendChannelToken);
    }
    @Test public void readsEpisodeAndCreatorTargetsInsteadOfCollectionId() {
        MenuPanelParser.Result result = MenuPanelParser.parse(fixture("browse-video"));
        assertEquals("redacted:28460223192466ef", result.videoId);
        assertEquals("redacted:26f79e42e21c9132", result.channelId);
        assertNotEquals("redacted:373e8210d9d09c1b", result.channelId);
    }
    @Test public void labelsAndOrderingDoNotSelectCommands() {
        JsonObject f = fixture("video"); MenuPanelParser.Result expected = MenuPanelParser.parse(f);
        JsonArray rows = rows(f); rows.add(rows.remove(0));
        for (JsonElement row : rows) row.getAsJsonObject().getAsJsonObject("listItemViewModel").addProperty("title", "translated");
        MenuPanelParser.Result actual = MenuPanelParser.parse(f);
        assertEquals(expected.videoId, actual.videoId); assertEquals(expected.channelId, actual.channelId);
        assertEquals(expected.notInterestedToken, actual.notInterestedToken);
        assertEquals(expected.notRecommendChannelToken, actual.notRecommendChannelToken);
    }
    @Test public void missingActionDoesNotBorrowAnotherToken() {
        JsonObject f = fixture("video"); rows(f).remove(5);
        assertNotNull(MenuPanelParser.parse(f).notInterestedToken); assertNull(MenuPanelParser.parse(f).notRecommendChannelToken);
        f = fixture("video"); rows(f).remove(4);
        assertNull(MenuPanelParser.parse(f).notInterestedToken); assertNotNull(MenuPanelParser.parse(f).notRecommendChannelToken);
    }
    @Test public void unrelatedFeedbackIsNotRecommendationFeedback() {
        JsonObject f = fixture("video"); command(rows(f).get(4)).getAsJsonObject("feedbackEndpoint").remove("actions");
        assertNull(MenuPanelParser.parse(f).notInterestedToken);
    }
    @Test public void ambiguousTokensFailClosed() {
        for (int index : new int[]{4, 5}) {
            JsonObject f = fixture("video"); rows(f).add(rows(f).get(index).deepCopy());
            assertNull(MenuPanelParser.parse(f).notInterestedToken); assertNull(MenuPanelParser.parse(f).notRecommendChannelToken);
        }
    }
    @Test public void conflictingVideoIdentityFailsClosed() {
        JsonObject f = fixture("video"); JsonElement row = rows(f).get(0).deepCopy();
        command(row).getAsJsonObject("commandExecutorCommand").getAsJsonArray("commands").get(1).getAsJsonObject()
                .getAsJsonObject("watchEndpoint").addProperty("videoId", "another-video");
        rows(f).add(row);
        MenuPanelParser.Result actual = MenuPanelParser.parse(f);
        assertNull(actual.videoId); assertNull(actual.notInterestedToken); assertNull(actual.notRecommendChannelToken);
    }
    @Test public void nullOrMissingPanelsDoNotInventActions() {
        for (JsonElement f : new JsonElement[]{null, JsonNull.INSTANCE, new JsonObject()}) {
            assertNull(MenuPanelParser.parse(f).videoId); assertNull(MenuPanelParser.parse(f).notInterestedToken);
        }
        JsonObject f = fixture("video"); rows(f).add(JsonNull.INSTANCE);
        assertNotNull(MenuPanelParser.parse(f).notInterestedToken);
    }
}
