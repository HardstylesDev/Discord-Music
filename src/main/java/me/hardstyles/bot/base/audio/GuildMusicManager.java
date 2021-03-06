package me.hardstyles.bot.base.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.guild.GuildObj;
import net.dv8tion.jda.api.entities.Guild;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {

    public final AudioPlayer player;

    public final TrackScheduler scheduler;

    public GuildMusicManager(AudioPlayerManager manager, Bot bot, Guild guild) {
        player = manager.createPlayer();

        GuildObj g = bot.getGuildManager().getGuildFromId(guild.getId());
        player.setVolume((int) g.getVolume());

        scheduler = new TrackScheduler(player, bot, guild);
        player.addListener(scheduler);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}
