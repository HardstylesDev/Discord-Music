package me.hardstyles.bot.base.guild;

import lombok.Getter;
import lombok.Setter;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.guild.guildSettings.Setting;
import me.hardstyles.bot.base.guild.guildSettings.impl.ChatTypeSetting;
import me.hardstyles.bot.base.guild.guildSettings.impl.PrefixSetting;
import me.hardstyles.bot.base.guild.guildSettings.impl.VolumeSetting;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;

@Setter
@Getter

public class GuildObj {
    private final Bot bot;


    private final String id;


    private final PrefixSetting prefixSetting;
    private final VolumeSetting volumeSetting;
    private final ChatTypeSetting chatTypeSetting;


    private HashSet<Setting> settings;

    public GuildObj(@NotNull final Bot bot, @NotNull final String id) {
        this.bot = bot;
        this.id = id;

        this.settings = new HashSet<>();
        this.prefixSetting = new PrefixSetting(bot, "prefix");
        this.chatTypeSetting = new ChatTypeSetting(bot, "chatType");
        this.volumeSetting = new VolumeSetting(bot, "volume");

        settings.addAll(Arrays.asList(prefixSetting, chatTypeSetting, volumeSetting));


    }

    public String getPrefix() {
        return this.prefixSetting.get();
    }

    public void setPrefix(String a) {
        this.prefixSetting.set(a);
    }

    public double getVolume() {
        return Double.parseDouble(volumeSetting.get());
    }

    public void setvolume(double vol) {
        this.volumeSetting.set("" + vol);
    }

    public void setChatType(String chatType) {
        chatTypeSetting.set(chatType);
    }

    public ChatType getChatType() {
        return ChatType.valueOf(chatTypeSetting.get());
    }

}
