package me.hardstyles.bot.musicbot.commands.audio;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;

public class LoopCommand extends Command {
    private final Bot bot;

    public LoopCommand(Bot bot) {
        super("loop", Permission.ADMINISTRATOR, new String[]{"repeat"}, Category.MUSIC, "Keep repeating this song", null);
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
        if (s.scheduler.getLoopedTrack() != null) {
            e.reply(bot.getEmbedFactory().noLongerLooping(e, s.scheduler.getLoopedTrack()).build()).queue();
            s.scheduler.setLoopedTrack(null);
        } else {
            if (s == null || s.player == null || s.player.getPlayingTrack() == null) {
                e.reply(bot.getEmbedFactory().error(e, "There's no track playing which means I can't loop it.").build()).queue();
                return;
            }
            s.scheduler.setLoopedTrack(s.player.getPlayingTrack());
            e.reply(bot.getEmbedFactory().nowLooping(e, s.scheduler.getLoopedTrack()).build()).queue();

        }
    }
}
