package me.hardstyles.bot.musicbot.commands.audio;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.guild.GuildObj;
import net.dv8tion.jda.api.Permission;

@SuppressWarnings("unused")

public class ReplayCommand extends Command {
    private final Bot bot;

    public ReplayCommand(Bot bot) {
        super("replay", Permission.MESSAGE_READ, new String[]{"restart"}, Category.MUSIC, "Re-play the last song to enter the queue", null);
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
        GuildObj guildObj = bot.getGuildManager().getGuildFromId(e.getGuild().getId());
        if (guildObj.getLastQueued() == null) {
            e.reply("no song to be played.").queue();
            return;
        }
        bot.getAudioHandler().play(s, guildObj.getLastQueued().makeClone());
        bot.getEmbedFactory().added(e, guildObj.getLastQueued());

    }
}
