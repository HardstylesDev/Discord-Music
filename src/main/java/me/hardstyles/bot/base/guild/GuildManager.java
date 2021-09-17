package me.hardstyles.bot.base.guild;

import lombok.Getter;
import me.hardstyles.bot.Bot;

import java.util.HashMap;
import java.util.HashSet;


public class GuildManager {

    @Getter
    private HashMap<String, GuildObj> newGuilds;
    private final Bot bot;

    public GuildManager(Bot bot) {
        this.bot = bot;
        this.newGuilds = new HashMap<>();
    }

    public GuildObj getGuildFromId(String id) {
        if (newGuilds.containsKey(id)) {
            return newGuilds.get(id);
        }

        newGuilds.put(id, new GuildObj(bot, id));
        bot.getGuildSettingsHandler().read();
        return bot.getGuildManager().getGuildFromId(id);
    }
}
