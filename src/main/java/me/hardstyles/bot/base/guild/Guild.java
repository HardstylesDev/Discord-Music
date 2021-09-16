package me.hardstyles.bot.base.guild;

import com.google.gson.JsonObject;


public class Guild {
    private JsonObject guildJson;
    private long guildID;


    public Guild(final long guildID, final JsonObject guildJson) {

        this.guildID = guildID;
        this.guildJson = guildJson;

    }


    public JsonObject getGuildJson() {
        return guildJson;
    }

    public long getGuildID() {
        return guildID;
    }

    public void setGuildJson(JsonObject guildJson) {
        this.guildJson = guildJson;
    }

    public void setGuildID(Long id) {
        this.guildID = id;
    }


    public String getJsonValue(String string) {
        if (guildJson.has(string)) {
            return guildJson.get(string).getAsString();
        }
        return null;
    }

    public ChatType getChatType() {
        if (getJsonValue("ChatType") == null || ChatType.fromName(getJsonValue("ChatType")) == null) {
            return ChatType.DEFAULT;
        } else {
            return ChatType.fromName(getJsonValue("ChatType"));
        }
    }
}

