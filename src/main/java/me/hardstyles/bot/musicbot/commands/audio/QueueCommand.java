package me.hardstyles.bot.musicbot.commands.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class QueueCommand extends Command {
    private final Bot bot;

    public QueueCommand(Bot bot) {
        super("queue", Permission.UNKNOWN, new String[]{"listsongs", "songs"}, Category.MUSIC, "List the tracks in the queue", null);
        this.bot = bot;
        this.getOptions().add(new OptionData(OptionType.STRING, "clear", "Clears the queue").addChoice("Clear", "Clear"));
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {

        GuildMusicManager s = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());

        if (e.getArgs().length == 0 && !e.hasInput("clear")) {


            if (s == null || s.player == null || s.player.getPlayingTrack() == null) {
                e.reply("The queue is empty!").queue();
                return;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(s.player.getPlayingTrack().getInfo().title);
            String p = bot.getFormatFactory().getProgressBar(s.player.getPlayingTrack().getPosition(), s.player.getPlayingTrack().getDuration(), 20, "<:yt_red:639530265403981824>", "<:yt_gray:639530265383010324>", "<:yt_ball:639530265399787530>");
            builder.append("\n").append(p);
            builder.append("\n").append(bot.getFormatFactory().formatDate(s.player.getPlayingTrack().getPosition())).append(" / ").append(bot.getFormatFactory().formatDate(s.player.getPlayingTrack().getDuration()));
            builder.append("\n");
            int index = 1;
            long totalDuration = s.player.getPlayingTrack().getDuration() - s.player.getPlayingTrack().getPosition();
            for (AudioTrack audioTrack : s.scheduler.getQueue()) {
                builder.append(String.format("\n`%s` %s (%s)", index, audioTrack.getInfo().title, bot.getFormatFactory().formatDate(audioTrack.getDuration())));
                index++;
                totalDuration = totalDuration + audioTrack.getDuration();
                if (index >= 15)
                    break;
            }
            builder.append("\n\nTotal duration: **").append(bot.getFormatFactory().formatDate(totalDuration)).append("**");
            e.reply(bot.getEmbedFactory().msg(e, "Queue", builder.toString()).setThumbnail("https://img.youtube.com/vi/" + s.player.getPlayingTrack().getIdentifier() + "/hqdefault.jpg").build()).queue();
            return;
        }
        if (e.getArgs() != null && e.getArgs().length > 0 && e.getArgs()[0].equalsIgnoreCase("clear") || e.hasInput("clear") && e.textInput("clear").equalsIgnoreCase("clear")) {
            GuildMusicManager m = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());
            m.scheduler.resetQueue();
            e.reply(bot.getEmbedFactory().msg(e, "Queue was cleared", "You've succesfully cleared the queue.").build()).queue();
        }


    }
}