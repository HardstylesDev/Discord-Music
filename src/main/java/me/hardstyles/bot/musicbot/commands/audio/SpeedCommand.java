package me.hardstyles.bot.musicbot.commands.audio;

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.commands.impl.input.impl.BooleanInput;
import me.hardstyles.bot.base.commands.impl.input.impl.NumberInput;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;

@SuppressWarnings("unused")

public class SpeedCommand extends Command {
    private final Bot bot;

    public SpeedCommand(Bot bot) {
        super("speed", Permission.MESSAGE_READ, new String[]{"setspeed"}, Category.MUSIC, "Set the speed of the music player.", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
        this.getSlashOptions().add(new OptionData(OptionType.NUMBER, "speed", "The speed"));
        this.getSlashOptions().add(new OptionData(OptionType.NUMBER, "pitch", "The pitch"));
        this.getSlashOptions().add(new OptionData(OptionType.BOOLEAN, "pitchsemitones", "Change the Pitch type to use semi tones, sort of like vaporwave. Change the pitch to ~ -7."));
    }


    @Override
    public void execute(CommandContext e) {
        if (bot.getVoiceCheck().voiceCheck(e.getMember()) || bot.getVoiceCheck().isPlaying(e.getMember(), bot)) {
            e.reply(bot.getEmbedFactory().error(e, "You're not in the same channel as me, so you can't execute this.").build()).queue();
            return;
        }
        NumberInput inputSpeed = new NumberInput(e, 1);
        NumberInput inputPitch = new NumberInput(e, 2);
        BooleanInput booleanInput = new BooleanInput(e, 3);
        double speed = inputSpeed.value("speed");
        double pitch = inputPitch.value("pitch");
        boolean vaporwave = booleanInput.value("pitchsemitones");

        GuildMusicManager guildMusicManager = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());
        if (speed == -9999 || speed == 0) {
            guildMusicManager.player.setFilterFactory(null);
            e.reply("Speed effect reverted, it's back to normal!").queue();
            return;
        }


        guildMusicManager.player.setFilterFactory((track, format, output) -> {
            TimescalePcmAudioFilter audioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
            audioFilter.setSpeed(speed); //1.5x normal speed
            if (pitch != -9999) {
                if (vaporwave) audioFilter.setPitchSemiTones(pitch);
                else audioFilter.setPitch(pitch);
            }
            return Collections.singletonList(audioFilter);
        });
        guildMusicManager.player.getPlayingTrack().setPosition(guildMusicManager.player.getPlayingTrack().getPosition());

        e.reply(bot.getEmbedFactory().msg(e, "Speed changed!", "Speed: `" + speed + (pitch != -9999 ? "`, Pitch: `" + pitch + "`" : "`") + (vaporwave ? " Semitones enabled" : "")).build()).queue();

    }
}
