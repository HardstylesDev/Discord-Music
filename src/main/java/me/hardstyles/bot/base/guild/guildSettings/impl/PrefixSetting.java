package me.hardstyles.bot.base.guild.guildSettings.impl;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.guild.guildSettings.Setting;
import org.jetbrains.annotations.NotNull;

public class PrefixSetting extends Setting {
    private final Bot bot;

    public PrefixSetting(@NotNull final Bot bot, @NotNull final String name) {
        super(bot, name);
        this.bot = bot;
    }

    private String prefix = ".";

    @Override
    public void set(String s) {
        this.prefix = s;
    }

    @Override
    public String get() {
        return this.prefix;
    }
}
