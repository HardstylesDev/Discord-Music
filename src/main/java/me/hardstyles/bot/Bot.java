package me.hardstyles.bot;

import lombok.Getter;
import me.hardstyles.bot.base.audio.CustomAudioHandler;
import me.hardstyles.bot.base.commands.CommandManager;
import me.hardstyles.bot.base.factory.EmbedFactory;
import me.hardstyles.bot.base.factory.FormatFactory;
import me.hardstyles.bot.base.guild.GuildManager;
import me.hardstyles.bot.base.guild.GuildSettingsHandler;
import me.hardstyles.bot.base.spotify.SpotifyAPI;
import me.hardstyles.bot.base.util.VoiceCheck;
import me.hardstyles.bot.musicbot.event.EventListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

@Getter
public class Bot {
    public static Bot instance = null;
    private CommandManager commandManager;

    private EmbedFactory embedFactory;
    private CustomAudioHandler audioHandler;
    private ShardManager shardManager;
    private GuildManager guildManager;
    private VoiceCheck voiceCheck;
    private FormatFactory formatFactory;
    private SpotifyAPI spotifyApi;

    private GuildSettingsHandler guildSettingsHandler;

    public Bot() {
        instance = this;
        spotifyApi = new SpotifyAPI(this);
        guildManager = new GuildManager(this);
        formatFactory = new FormatFactory(this);
        voiceCheck = new VoiceCheck();
        registerManagers();

        loadBot();

        this.guildSettingsHandler = new GuildSettingsHandler(this);


    }


    private void registerManagers() {
        commandManager = new CommandManager(this);
        commandManager.initialize();
        embedFactory = new EmbedFactory(this);
        audioHandler = new CustomAudioHandler(this);

    }

    private void loadBot() {
        try {
            this.shardManager = DefaultShardManagerBuilder.createDefault(getToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES).setStatus(OnlineStatus.ONLINE).enableCache(CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY).setMemberCachePolicy(MemberCachePolicy.ALL).setShardsTotal(1).enableCache(CacheFlag.MEMBER_OVERRIDES).setActivity(Activity.listening("TheBlitzBot.com")).addEventListeners(new EventListener(this)).build();
        } catch (Exception ignored) {
        }
        System.out.println("Bot online");

    }


    private String getToken() {
        try {
            File file = new File("Token.txt");
            Scanner scanner = new Scanner(file);
            return scanner.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
         new Bot();
    }
}
