package me.hardstyles.bot;

import lombok.Getter;
import me.hardstyles.bot.base.audio.CustomAudioHandler;
import me.hardstyles.bot.base.commands.CommandManager;
import me.hardstyles.bot.base.configuration.PrefixManager;
import me.hardstyles.bot.base.database.Database;
import me.hardstyles.bot.base.factory.EmbedFactory;
import me.hardstyles.bot.base.factory.FormatFactory;
import me.hardstyles.bot.base.guild.GuildManager;
import me.hardstyles.bot.base.util.VoiceCheck;
import me.hardstyles.bot.musicbot.commands.TestCommand;
import me.hardstyles.bot.musicbot.commands.admin.PrefixCommand;
import me.hardstyles.bot.musicbot.commands.admin.SettingsCommand;
import me.hardstyles.bot.musicbot.commands.audio.*;
import me.hardstyles.bot.musicbot.event.EventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

@Getter
public class Bot {
    public static Bot instance = null;
    private CommandManager commandManager;
    private PrefixManager prefixManager;
    private EmbedFactory embedFactory;
    private CustomAudioHandler audioHandler;
    private Database database;
    private ShardManager shardManager;
    private GuildManager guildManager;
    private VoiceCheck voiceCheck;
    private FormatFactory formatFactory;

    public Bot() {
        instance = this;

        this.database = new Database();
        guildManager=  new GuildManager(this);
        formatFactory = new FormatFactory(this);
        voiceCheck = new VoiceCheck();
        registerManagers();


        prefixManager.loadPrefixes();
        startBot();


        new PlayCommand(this);
        new TestCommand(this);
        new StopCommand(this);
        new PrefixCommand(this);
        new ClearCommand(this);
        new SettingsCommand(this);
        new LoopCommand(this);
        new PauseCommand(this);
        new SkipCommand(this);
        new QueueCommand(this);
        new BassBoostCommand(this);


    }


    private void registerManagers() {
        commandManager = new CommandManager(this);
        prefixManager = new PrefixManager(this);
        embedFactory = new EmbedFactory(this);
        audioHandler = new CustomAudioHandler(this);
    }

    private void startBot() {
        System.out.println("[SYSTEM]: Loading...");
        long milis = System.currentTimeMillis();
        try {
            this.shardManager = DefaultShardManagerBuilder.createDefault("NTEwNzkxOTI3NjYzNDI3NTk0.W-bODQ.bVoaQM4YA2MVVmx4WFv_4m_8iac", GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES).setStatus(OnlineStatus.ONLINE).enableCache(CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY).setMemberCachePolicy(MemberCachePolicy.ALL).setShardsTotal(1).enableCache(CacheFlag.MEMBER_OVERRIDES).setActivity(Activity.listening("TheBlitzBot.com")).addEventListeners(new EventListener(this)).build();
            JDA jda = null;
            jda.updateCommands().queue();
        } catch (Exception ignored) {
        }
        long milis2 = System.currentTimeMillis();
        long diff = milis2 - milis;
        System.out.println("[SYSTEM]: Blitz Beats loaded, time: " + diff + "ms");
    }

    public static void main(String[] args) {
        new Bot();

    }


}