package me.hardstyles.bot.base.factory;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.guild.ChatType;
import me.hardstyles.bot.base.guild.GuildObj;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

//piece of shit class copied from older project
public class EmbedFactory {
    private final Bot bot;

    public EmbedFactory(@NotNull Bot bot) {
        this.bot = bot;
    }

    public EmbedBuilder coloredEmbed(net.dv8tion.jda.api.entities.Guild guild) {
        Color c = fromGuild(guild);
        if (c == null || c.getRGB() == 0) {
            c = new Color(100, 0, 50);
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(c);
        return builder;
    }

    public EmbedBuilder noPermission(Member m, Permission p) {
        EmbedBuilder builder = this.coloredEmbed(m.getGuild());
        builder.setTitle("No permission!");
        builder.setDescription("Sorry " + m.getAsMention() + ", but you need the `" + p.getName() + "` permission!");
        return builder;

    }

    public EmbedBuilder simple(net.dv8tion.jda.api.entities.Guild g, String title, String message) {
        EmbedBuilder builder = coloredEmbed(g);
        builder.setTitle(title);
        builder.setDescription(message);
        return builder;
    }

    private Color fromGuild(net.dv8tion.jda.api.entities.Guild g) {
        if (g.getSelfMember().getColor() == null || g.getSelfMember().getColor().getRGB() == 0) {
            return new Color(100, 0, 50);
        }
        return g.getSelfMember().getColor();

    }


    public void added(CommandContext msg, AudioTrack audioTrack) {
        GuildMusicManager s = bot.getAudioHandler().getMusicManagers().get(msg.getGuild().getIdLong());
        GuildObj guild = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());
        if (guild.getChatType() == ChatType.DEFAULT) {
            EmbedBuilder embedBuilder = this.coloredEmbed(msg.getGuild());
            embedBuilder.setThumbnail("https://img.youtube.com/vi/" + audioTrack.getIdentifier() + "/hqdefault.jpg");
            embedBuilder.setTitle("Track added");

            String stringBuilder = "\nAdded `" + audioTrack.getInfo().title + "` to the queue!" +
                    "\n\n`•` Uploaded by: **" + audioTrack.getInfo().author + "**" +
                    "\n`•` Length: **" + bot.getFormatFactory().formatDate(audioTrack.getDuration()) + "**" +
                    "\n`•` Position: **" + (s.scheduler.getQueue().size() + 1) + "**" +
                    "\n`•` Requested by: **" + msg.getUser().getAsMention() + "**";
            embedBuilder.setDescription(stringBuilder);
            msg.reply(embedBuilder.build()).queue();
            return;
        } else if (guild.getChatType() == ChatType.SIMPLE) {
            msg.reply("" + msg.getUser().getAsMention() + " added **" + audioTrack.getInfo().title + "** to the queue").queue();
            return;
        } else if (guild.getChatType() == ChatType.REACTION) {
            msg.reply("\uD83D\uDC4C").queue();
        }

    }

    public EmbedBuilder addedPlaylist(CommandContext msg, AudioPlaylist audioTrack) {
        GuildMusicManager s = bot.getAudioHandler().getMusicManagers().get(msg.getGuild().getIdLong());
        EmbedBuilder embedBuilder = this.coloredEmbed(msg.getGuild());
        embedBuilder.setTitle("Playlist added");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nAdded " + audioTrack.getTracks().size() + " tracks to the queue!");
        stringBuilder.append("\n\n`•` Requested by: " + msg.getUser().getAsMention());
        embedBuilder.setDescription(stringBuilder.toString());

        return embedBuilder;
    }

    public EmbedBuilder noLongerLooping(CommandContext msg, AudioTrack audioTrack) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("No longer looping");
        String stringBuilder = "\nTrack " + audioTrack.getInfo().title + " is no longer looping!" +
                "\n\n`•` Requested by: " + msg.getUser().getAsMention();
        embedBuilder.setDescription(stringBuilder);
        embedBuilder.setDescription(stringBuilder.toString());
        return embedBuilder;
    }

    public EmbedBuilder nowLooping(CommandContext msg, AudioTrack audioTrack) {
        GuildMusicManager s = bot.getAudioHandler().getMusicManagers().get(msg.getGuild().getIdLong());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Now looping");
        String stringBuilder = "\nTrack " + audioTrack.getInfo().title + " is now being looped!" +
                "\n\n`•` Requested by: " + msg.getUser().getAsMention();
        embedBuilder.setDescription(stringBuilder);
        embedBuilder.setDescription(stringBuilder.toString());
        return embedBuilder;
    }

    public EmbedBuilder error(CommandContext msg, String error) {
        GuildMusicManager s = bot.getAudioHandler().getMusicManagers().get(msg.getGuild().getIdLong());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Error");
        embedBuilder.setDescription(error);
        embedBuilder.setColor(new Color(246, 70, 70));
        return embedBuilder;
    }

    public EmbedBuilder msg(CommandContext ctx, String title, String message) {
        EmbedBuilder embedBuilder = this.coloredEmbed(ctx.getGuild());
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        return embedBuilder;
    }

    public void stopped(CommandContext msg) {
        GuildObj guild = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());
        if (guild.getChatType() == ChatType.DEFAULT) {
            EmbedBuilder embedBuilder = this.coloredEmbed(msg.getGuild());

            embedBuilder.setTitle("Stopped");
            embedBuilder.setDescription("Stopped the player and cleared the queue!" +
                    "\n\n`•` Stopped by:" + msg.getUser().getAsMention());
            msg.reply(embedBuilder.build()).queue();
            return;
        } else if (guild.getChatType() == ChatType.SIMPLE) {
            msg.reply("" + msg.getUser().getAsMention() + " stopped the music & cleared the queue").queue();
            return;
        } else if (guild.getChatType() == ChatType.REACTION) {
            msg.reply("\uD83D\uDDD1").queue();
        }


    }

    public void leave(CommandContext msg) {
        GuildObj guild = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());
        if (guild.getChatType() == ChatType.DEFAULT) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("I left!");
            embedBuilder.setDescription("Disconnected from the channel. Also stopped the player and cleared the queue!" +
                    "\n\n`•` Stopped by:" + msg.getUser().getAsMention());
            msg.reply(embedBuilder.build()).queue();
        } else if (guild.getChatType() == ChatType.SIMPLE) {
            msg.reply("" + msg.getUser().getAsMention() + " I left, stopped the music and cleared the queue").queue();
        } else if (guild.getChatType() == ChatType.REACTION) {
            msg.reply("\uD83D\uDC4B").queue();
        }
    }

    public void nothingInQueue(CommandContext msg) {
        GuildObj guild = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());
        if (guild.getChatType() == ChatType.DEFAULT) {
            EmbedBuilder embedBuilder = this.coloredEmbed(msg.getGuild());
            embedBuilder.setTitle("Queue is empty!");
            embedBuilder.setDescription("There's nothing in the queue, therefore I can't skip!" +
                    "\n\n`•` Requested by:" + msg.getUser().getAsMention());
            msg.reply(embedBuilder.build()).queue();
        } else if (guild.getChatType() == ChatType.SIMPLE || guild.getChatType() == ChatType.REACTION) {
            msg.reply("" + msg.getUser().getAsMention() + " The queue is empty, can't skip!").queue();

        }
    }

    public void pause(CommandContext msg, boolean paused) {
        GuildObj guild = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());
        GuildMusicManager s = bot.getAudioHandler().getMusicManagers().get(msg.getGuild().getIdLong());
        if (guild.getChatType() == ChatType.DEFAULT) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((paused ? "Paused" : "Resumed") + " " + s.player.getPlayingTrack().getInfo().title);
            String p = bot.getFormatFactory().getProgressBar(s.player.getPlayingTrack().getPosition(), s.player.getPlayingTrack().getDuration(), 20, "<:yt_red:639530265403981824>", "<:yt_gray:639530265383010324>", "<:yt_ball:639530265399787530>");
            stringBuilder.append("\n\n" + p);
            stringBuilder.append("\n" + bot.getFormatFactory().formatDate(s.player.getPlayingTrack().getPosition()) + " / " + bot.getFormatFactory().formatDate(s.player.getPlayingTrack().getDuration()));
            msg.reply(msg(msg, (paused ? "Paused" : "Resumed"), stringBuilder.toString()).setThumbnail("https://img.youtube.com/vi/" + s.player.getPlayingTrack().getIdentifier() + "/hqdefault.jpg").build()).queue();
        } else if (guild.getChatType() == ChatType.SIMPLE) {
            msg.reply("" + msg.getUser().getAsMention() + " paused the track.").queue();
        } else if (guild.getChatType() == ChatType.REACTION) {
            msg.reply(paused ? "\u23F8" : "\u23EF").queue();
        }
    }


    public void skippingTrack(CommandContext msg) {

        GuildObj guild = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());
        GuildMusicManager s = bot.getAudioHandler().getMusicManagers().get(msg.getGuild().getIdLong());
        if (guild.getChatType() == ChatType.DEFAULT) {
            EmbedBuilder embedBuilder = this.coloredEmbed(msg.getGuild());
            embedBuilder.setTitle("Skipped track!");
            embedBuilder.setDescription("Skipped track! Now playing **" + s.scheduler.getNextInLine().getInfo().title + "**\n\nSkipped by: " + msg.getUser().getAsMention());
            msg.reply(embedBuilder.build()).queue();
        } else if (guild.getChatType() == ChatType.SIMPLE) {
            msg.reply("" + msg.getUser().getAsMention() + "skipped a track! Now playing **" + s.scheduler.getNextInLine().getInfo().title + "**").queue();
        } else if (guild.getChatType() == ChatType.REACTION) {
            msg.reply("\uD83D\uDC4C").queue();
        }

    }
}
