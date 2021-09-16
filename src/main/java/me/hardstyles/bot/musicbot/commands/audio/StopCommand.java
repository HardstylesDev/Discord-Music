package me.hardstyles.bot.musicbot.commands.audio;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;

public class StopCommand extends Command {
    private final Bot bot;

    public StopCommand(Bot bot) {
        super("stop", Permission.ADMINISTRATOR, new String[]{"leave", "quit"}, Category.MUSIC, "Clear the queue, leave the channel.", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {
        if(bot.getVoiceCheck().voiceCheck(e.getMember())){
            e.reply(bot.getEmbedFactory().error(e,"You're not in the same channel as me, so you can't execute this.").build()).queue();
            return;
        }
        GuildMusicManager guildMusicManager = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());
        if (guildMusicManager == null || guildMusicManager.player == null || guildMusicManager.player.getPlayingTrack() == null){
            e.getGuild().getAudioManager().closeAudioConnection();
            return;
        }

        guildMusicManager.player.stopTrack();
        guildMusicManager.scheduler.resetQueue();
        e.getGuild().getAudioManager().closeAudioConnection();
        bot.getEmbedFactory().stopped(e);
    }
}
