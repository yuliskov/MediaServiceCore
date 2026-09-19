package com.liskovsoft.youtubeapi.menu;
import com.google.gson.*;
/** Reads command targets, independently of labels, locale, and menu ordering. */
public final class MenuPanelParser {
    public static final class Result {
        public final String videoId, channelId, notInterestedToken, notRecommendChannelToken;
        Result(String videoId, String channelId, String[] feedback) {
            this.videoId = videoId;
            this.channelId = channelId;
            notInterestedToken = feedback[0];
            notRecommendChannelToken = feedback[1];
        }
    }
    public static Result parse(JsonElement raw) {
        String videoId = null, channelId = null;
        JsonElement rows = at(raw, "content", "engagementPanelSectionListRenderer", "content", "listViewModel", "listItems");
        if (rows != null && rows.isJsonArray()) for (JsonElement row : rows.getAsJsonArray()) {
            JsonElement commands = at(row, "listItemViewModel", "rendererContext", "commandContext", "onTap", "innertubeCommand", "commandExecutorCommand", "commands");
            if (commands != null && commands.isJsonArray()) for (JsonElement command : commands.getAsJsonArray()) {
                String video = string(at(command, "watchEndpoint", "videoId"));
                String channel = string(at(command, "browseEndpoint", "browseId"));
                if (video != null) {
                    if (videoId != null && !videoId.equals(video)) return new Result(null, null, new String[]{null, null});
                    videoId = video;
                }
                if (channel != null) {
                    if (channelId != null && !channelId.equals(channel)) return new Result(null, null, new String[]{null, null});
                    channelId = channel;
                }
            }
        }
        return new Result(videoId, channelId, parseFeedback(raw));
    }
    private static String string(JsonElement value) {
        return value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isString() && !value.getAsString().isEmpty() ? value.getAsString() : null;
    }
    private static JsonElement at(JsonElement value, String... keys) {
        for (String key : keys) {
            if (value == null || !value.isJsonObject()) return null;
            value = value.getAsJsonObject().get(key);
        }
        return value;
    }
    private static boolean isDismissalFeedback(JsonElement feedback) {
        JsonElement actions = at(feedback, "actions");
        if (actions == null || !actions.isJsonArray()) return false;
        for (JsonElement action : actions.getAsJsonArray()) {
            JsonElement type = at(action, "openClientOverlayAction", "type");
            if (type != null && "CLIENT_OVERLAY_TYPE_DISMISSAL_FOLLOW_UP".equals(type.getAsString())) return true;
        }
        return false;
    }
    private static boolean isChannelConfirmation(JsonElement feedback) {
        JsonElement actions = at(feedback, "actions");
        if (actions == null || !actions.isJsonArray()) return false;
        boolean removesItem = false, showsMessage = false;
        for (JsonElement action : actions.getAsJsonArray()) {
            removesItem |= at(action, "removeItemAction", "childId") != null;
            showsMessage |= at(action, "openPopupAction", "popup", "overlayMessageRenderer") != null;
        }
        return removesItem && showsMessage;
    }
    static String[] parseFeedback(JsonElement raw) {
        String videoToken = null, channelToken = null;
        JsonElement list = at(raw, "content", "engagementPanelSectionListRenderer", "content", "listViewModel", "listItems");
        if (list == null || !list.isJsonArray()) return new String[]{null,null};
        for (JsonElement row : list.getAsJsonArray()) {
            JsonElement command = at(row, "listItemViewModel", "rendererContext", "commandContext", "onTap", "innertubeCommand");
            JsonElement direct = at(command, "feedbackEndpoint", "feedbackToken");
            if (direct != null && isDismissalFeedback(at(command, "feedbackEndpoint"))) {
                if (videoToken != null) return new String[]{null, null};
                videoToken = string(direct);
            }
            JsonElement confirmation = at(command, "openPopupAction", "popup", "overlaySectionRenderer", "overlay", "overlayTwoPanelRenderer",
                    "actionPanel", "overlayPanelRenderer", "content", "overlayPanelItemListRenderer", "items");
            if (confirmation != null && confirmation.isJsonArray()) for (JsonElement choice : confirmation.getAsJsonArray()) {
                JsonElement commands = at(choice, "compactLinkRenderer", "serviceEndpoint", "commandExecutorCommand", "commands");
                if (commands != null && commands.isJsonArray()) for (JsonElement step : commands.getAsJsonArray()) {
                    JsonElement token = at(step, "feedbackEndpoint", "feedbackToken");
                    if (token != null && isChannelConfirmation(at(step, "feedbackEndpoint"))) {
                        if (channelToken != null) return new String[]{null, null};
                        channelToken = string(token);
                    }
                }
            }
        }
        return new String[]{videoToken, channelToken};
    }
}
