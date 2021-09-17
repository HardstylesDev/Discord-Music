package me.hardstyles.bot.base.guild.guildSettings;

import lombok.Getter;
import me.hardstyles.bot.Bot;
import org.jetbrains.annotations.NotNull;

public class Setting {
    private final Bot bot;
    @Getter

    private final String name;
    public Setting(@NotNull final Bot bot,@NotNull final  String name) {
        this.bot = bot;
        this.name = name;
    }


    public void set(String val) {
    }

    public String get() {
        return null;
    }
}
