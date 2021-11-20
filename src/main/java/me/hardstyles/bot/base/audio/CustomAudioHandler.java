package me.hardstyles.bot.base.audio;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.guild.GuildObj;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")

public class CustomAudioHandler {

    private EqualizerFactory equalizer;
    private AudioPlayer audioPlayer;
    private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private final Bot bot;

    public CustomAudioHandler(Bot bot) {
        this.bot = bot;
        playerManager.getConfiguration().setFilterHotSwapEnabled(true);
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        this.equalizer = new EqualizerFactory();
        this.audioPlayer = playerManager.createPlayer();
    }

    private final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    public Map<Long, GuildMusicManager> getMusicManagers() {
        return this.musicManagers;
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {


            musicManager = new GuildMusicManager(playerManager, bot, guild);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    /**
     * Loads and plays a track by it's URL.
     *
     * @param trackUrl The URL of the track you want to play
     */

    public void loadAndPlay(final CommandContext ctx, final String trackUrl) {
        loadAndPlay(ctx, trackUrl, false);
    }

    /**
     * Loads and plays a track by it's URL.
     *
     * @param msg        The message to grab details from.
     * @param trackUrl   The URL of the track you want to play
     * @param isPlaylist Set if the url should be loaded as a playlist
     */

    public void loadAndPlay(final CommandContext msg, final String trackUrl, boolean isPlaylist) {
        if (!msg.getGuild().getAudioManager().isConnected() && !msg.getGuild().getAudioManager().isAttemptingToConnect()) {
            if (msg.getMember().getVoiceState().getChannel() == null) {
                msg.reply(bot.getEmbedFactory().simple(msg.getGuild(), "Not connected", "You're not connected to a channel!").build()).queue();
                return;
            }
            if (!msg.getGuild().getSelfMember().hasPermission(msg.getMember().getVoiceState().getChannel(), Permission.VOICE_CONNECT)) {
                msg.reply(bot.getEmbedFactory().msg(msg, "Can't connect", "I'm not allowed to join your voice channel!").build()).queue();
                return;
            }
            msg.getGuild().getAudioManager().openAudioConnection(msg.getMember().getVoiceState().getChannel());
        }

        GuildMusicManager musicManager = getGuildAudioPlayer(msg.getGuild());
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                bot.getEmbedFactory().added(msg, track);
                play(musicManager, track);
                GuildObj guildObj = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());
                guildObj.setLastQueued(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }
                if (isPlaylist) {
                    List<AudioTrack> playlistTracks = playlist.getTracks();
                    play(musicManager, firstTrack);

                    playlistTracks.remove(firstTrack);
                    GuildObj guildObj = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());

                    playlist.getTracks().forEach(audioTrack -> {
                        play(musicManager, audioTrack);
                        guildObj.setLastQueued(audioTrack);
                    });
                    msg.reply(bot.getEmbedFactory().addedPlaylist(msg, playlist).build()).queue();


                } else {
                    play(musicManager, firstTrack);
                    bot.getEmbedFactory().added(msg, firstTrack);
                    GuildObj guildObj = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());
                    guildObj.setLastQueued(firstTrack);

                }


            }

            @Override
            public void noMatches() {
                msg.reply("Couldn't find any tracks matching `" + trackUrl + "`").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                msg.reply("Could not play: " + exception.getMessage()).queue();
                exception.printStackTrace();
            }
        });
    }

    public void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }


    /**
     * Skip the track that's currently playing (if playing at all)
     *
     * @param msg The message to grab details from.
     */
    public void skipTrack(CommandContext msg) {
        GuildMusicManager musicManager = getGuildAudioPlayer(msg.getTextChannel().getGuild());
        if (musicManager.scheduler.getNextInLine() == null) {
            bot.getEmbedFactory().nothingInQueue(msg);
        } else {
            bot.getEmbedFactory().skippingTrack(msg);
            musicManager.scheduler.nextTrack();
        }
    }

    public void pauseTrack(CommandContext msg) {
        GuildMusicManager musicManager = getGuildAudioPlayer(msg.getTextChannel().getGuild());
        this.pauseTrack(msg, !musicManager.player.isPaused());
    }


    public void loadPlaylist(final CommandContext msg, final String[] titles) {
        if (!msg.getGuild().getAudioManager().isConnected() && !msg.getGuild().getAudioManager().isAttemptingToConnect()) {
            if (msg.getMember().getVoiceState().getChannel() == null) {
                msg.reply(bot.getEmbedFactory().simple(msg.getGuild(), "Not connected", "You're not connected to a channel!").build()).queue();
                return;
            }
            if (!msg.getGuild().getSelfMember().hasPermission(msg.getMember().getVoiceState().getChannel(), Permission.VOICE_CONNECT)) {
                msg.reply(bot.getEmbedFactory().msg(msg, "Can't connect", "I'm not allowed to join your voice channel!").build()).queue();
                return;
            }
            msg.getGuild().getAudioManager().openAudioConnection(msg.getMember().getVoiceState().getChannel());
        }

        GuildMusicManager musicManager = getGuildAudioPlayer(msg.getGuild());


        String searchType = "ytsearch: ";
        for (String trackUrl : titles) {
            playerManager.loadItemOrdered(musicManager, searchType + trackUrl, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    msg.reply(bot.getEmbedFactory().addedPlaylist(msg, titles).build()).queue();
                    play(musicManager, track);
                    GuildObj guildObj = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());
                    guildObj.setLastQueued(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    AudioTrack firstTrack = playlist.getSelectedTrack();
                    if (firstTrack == null) {
                        firstTrack = playlist.getTracks().get(0);
                    }
                    play(musicManager, firstTrack);
                    GuildObj guildObj = bot.getGuildManager().getGuildFromId(msg.getGuild().getId());
                    guildObj.setLastQueued(firstTrack);
                }

                @Override
                public void noMatches() {
                    msg.reply("Nothing found by " + trackUrl).queue();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    msg.reply("Could not play: " + exception.getMessage()).queue();
                    exception.printStackTrace();
                }
            });
        }

    }

    /**
     * Pause the current track
     *
     * @param msg   The message to grab details from.
     * @param pause Set wether the player should be paused or resumed
     */

    public void pauseTrack(CommandContext msg, boolean pause) {
        GuildMusicManager musicManager = getGuildAudioPlayer(msg.getTextChannel().getGuild());
        if (musicManager.player.getPlayingTrack() == null) {
            msg.reply("nothing to be paused ").queue();
            return;
        }

        bot.getEmbedFactory().pause(msg, pause);
        musicManager.player.setPaused(pause);
    }

    // public static void connectToFirstVoiceChannel(AudioManager audioManager) {
    //     if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
    //         for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
    //             audioManager.openAudioConnection(voiceChannel);
    //             break;
    //         }
    //     }
    // }

}
