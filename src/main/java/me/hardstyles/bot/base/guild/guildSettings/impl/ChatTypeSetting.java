package me.hardstyles.bot.base.guild.guildSettings.impl;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.guild.guildSettings.Setting;
import org.jetbrains.annotations.NotNull;

public class ChatTypeSetting extends Setting{
    private final Bot bot;
    public ChatTypeSetting(@NotNull final Bot bot, @NotNull final String name) {
        super(bot, name);
        this.bot = bot;
    }

    private String chatType = "DEFAULT";
    @Override
    public void set(String s){
        this.chatType = s;
    }

    @Override
    public String get(){
        return this.chatType;
    }

    @Override
    public String defaultValue(){
        return "DEFAULT";
    }

}
