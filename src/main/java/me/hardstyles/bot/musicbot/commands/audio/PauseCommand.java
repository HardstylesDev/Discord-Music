package me.hardstyles.bot.musicbot.commands.audio;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;

public class PauseCommand extends Command {
    private final Bot bot;

    public PauseCommand(Bot bot) {
        super("pause", Permission.MESSAGE_READ, new String[]{"unpause"}, Category.MUSIC, "Skip to the next track", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {
        if (bot.getVoiceCheck().voiceCheck(e.getMember())) {
            e.reply(bot.getEmbedFactory().error(e, "You're not in the same channel as me, so you can't execute this.").build()).queue();
            return;
        }
        GuildMusicManager s = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());
        if(s == null || s.player == null || s.player.getPlayingTrack() == null){
            e.reply(bot.getEmbedFactory().error(e,"There's no track playing which means I can't pause/resume it.").build()).queue();

            return;
        }
        bot.getAudioHandler().pauseTrack(e);
    }
}
