package me.hardstyles.bot.base.audio;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mysql.cj.util.TimeUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.hardstyles.bot.Bot;
import net.dv8tion.jda.api.entities.Guild;

import java.util.concurrent.*;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private static final ThreadFactoryBuilder THREAD_FACTORY = new ThreadFactoryBuilder().setUncaughtExceptionHandler((t, e) -> System.out.println(String.format("There was an exception in thread %s: %s", t.getName(), e.getMessage())));
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY.setNameFormat("Cooldowns").build());
    private AudioTrack loop = null;
    private final Guild guild;

    public AudioTrack getLoopedTrack() {
        return loop;
    }

    public void setLoopedTrack(AudioTrack a) {
        loop = a;
    }

    public TrackScheduler(AudioPlayer player, Bot bot, Guild guild) {
        this.guild = guild;
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void resetQueue() {
        queue.clear();
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public AudioTrack getNextInLine() {

        AudioTrack track = null;
        for (AudioTrack audioTrack : queue) {
            track = audioTrack;
            break;
        }
        return track;

    }

    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.

        player.startTrack(queue.poll(), false);
    }



    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (loop != null) {
                player.startTrack(loop.makeClone(), false);
            } else {
                if(queue.size() == 0){
                    EXECUTOR.schedule(() -> {
                        if(queue.size() == 0 || player.getPlayingTrack() == null){
                            guild.getAudioManager().closeAudioConnection();
                        }
                    }, 1, TimeUnit.MINUTES);
                }
                nextTrack();
            }
        }

    }
}
