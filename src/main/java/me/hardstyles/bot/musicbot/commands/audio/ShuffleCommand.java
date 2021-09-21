package me.hardstyles.bot.musicbot.commands.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.Collections;

public class ShuffleCommand extends Command {
    private final Bot bot;

    public ShuffleCommand(Bot bot) {
        super("shuffle", Permission.MESSAGE_READ, new String[]{"shuff"}, Category.MUSIC, "Skip to the next track", null);
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
        if(s == null || s.scheduler == null || s.scheduler.getQueue().isEmpty()){
            e.reply(bot.getEmbedFactory().error(e,"There's no tracks in the queue.").build()).queue();
            return;
        }
        ArrayList<AudioTrack> list = new ArrayList<>();
        list.addAll(s.scheduler.getQueue());
        s.scheduler.getQueue().clear();
        Collections.shuffle(list);
        for (AudioTrack audioTrack : list) {
            try {
                s.scheduler.getQueue().put(audioTrack);
            } catch (InterruptedException interruptedException) {
               e.reply(interruptedException.getMessage()).queue();
            }
        }

       e.reply("Queue shuffled!").queue();
    }
}
