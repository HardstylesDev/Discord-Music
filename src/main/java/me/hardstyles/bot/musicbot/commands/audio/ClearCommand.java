package me.hardstyles.bot.musicbot.commands.audio;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.commands.impl.input.impl.NumberInput;
import net.dv8tion.jda.api.Permission;
@SuppressWarnings("unused")

public class ClearCommand extends Command {
    private final Bot bot;

    public ClearCommand(Bot bot) {
        super("clear", Permission.MESSAGE_READ, null, Category.MUSIC, "Clear the queue", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {
        if (bot.getVoiceCheck().voiceCheck(e.getMember())) {
            e.reply(bot.getEmbedFactory().error(e, "You're not in the same channel as me, so you can't execute this.").build()).queue();
            return;
        }



        GuildMusicManager m = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());
        m.scheduler.resetQueue();
        e.reply(bot.getEmbedFactory().msg(e,"Queue was cleared", "You've succesfully cleared the queue.").build()).queue();

    }
}
