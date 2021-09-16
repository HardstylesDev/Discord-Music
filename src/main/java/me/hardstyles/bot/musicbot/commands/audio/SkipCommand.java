package me.hardstyles.bot.musicbot.commands.audio;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;

public class SkipCommand extends Command {
    private final Bot bot;

    public SkipCommand(Bot bot) {
        super("skip", Permission.ADMINISTRATOR, new String[]{"next"}, Category.MUSIC, "Skip to the next track", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {
        if (bot.getVoiceCheck().voiceCheck(e.getMember())) {
            e.reply(bot.getEmbedFactory().error(e, "You're not in the same channel as me, so you can't execute this.").build()).queue();
            return;
        }
        bot.getAudioHandler().skipTrack(e);
    }
}
