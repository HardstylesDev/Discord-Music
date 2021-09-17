package me.hardstyles.bot.base.guild.guildSettings.impl;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.guild.guildSettings.Setting;
import org.jetbrains.annotations.NotNull;

public class VolumeSetting extends Setting{
    private final Bot bot;
    public VolumeSetting(@NotNull Bot bot, @NotNull final String name) {
        super(bot, name);
        this.bot = bot;
    }

    private String volume = "100";
    @Override
    public void set(String s){
        this.volume = s;
    }


    @Override
    public String get(){
        return this.volume;
    }

    @Override
    public String defaultValue(){
        return "100";
    }
}
