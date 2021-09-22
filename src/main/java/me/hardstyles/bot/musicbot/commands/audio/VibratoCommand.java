package me.hardstyles.bot.musicbot.commands.audio;

import com.github.natanbc.lavadsp.lowpass.LowPassPcmAudioFilter;
import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.FilterChainBuilder;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.commands.impl.input.impl.NumberInput;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class VibratoCommand extends Command {
    private final Bot bot;

    public VibratoCommand(Bot bot) {
        super("vibrate", Permission.MESSAGE_READ, new String[]{"vib"}, Category.MUSIC, "Set the speed of the music player.", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
        this.getOptions().add(new OptionData(OptionType.INTEGER, "value", "The value of the bassbooster."));
    }

    private static final float[] BASS_BOOST = {0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f,
            -0.1f, -0.1f, -0.1f, -0.1f};


    @Override
    public void execute(CommandContext e) {
        if (bot.getVoiceCheck().voiceCheck(e.getMember())) {
            e.reply(bot.getEmbedFactory().error(e, "You're not in the same channel as me, so you can't execute this.").build()).queue();
            return;
        }
        NumberInput numberInput = new NumberInput(e, 1);
        double input = numberInput.value("value");


        GuildMusicManager guildMusicManager = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());
        if (input == -9999 || input == 0) {
            guildMusicManager.player.setFilterFactory(null);
            e.reply("Bassboost has been disabled!").queue();
            return;
        }


        ;
        guildMusicManager.player.setFilterFactory((track, format, output) -> {
            FilterChainBuilder builder = new FilterChainBuilder();
            LowPassPcmAudioFilter lowPass = new LowPassPcmAudioFilter(output, format.channelCount, format.sampleRate);
            lowPass.setSmoothing(6f);
            TimescalePcmAudioFilter timeFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
            timeFilter.setSpeed(1.2);
            timeFilter.setPitch(1f);
            //apply those fucking things
            builder.addFirst(lowPass);
            builder.addFirst(timeFilter);

            return builder.build(null, format.channelCount).filters;
        });

        guildMusicManager.player.getPlayingTrack().setPosition(guildMusicManager.player.getPlayingTrack().getPosition());

        e.reply("Speed of player set to: " + input + " + pitch " + input).queue();

    }
}
