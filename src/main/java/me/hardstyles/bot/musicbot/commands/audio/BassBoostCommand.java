package me.hardstyles.bot.musicbot.commands.audio;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.commands.impl.input.impl.NumberInput;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
@SuppressWarnings("unused")

public class BassBoostCommand extends Command {
    private final Bot bot;

    public BassBoostCommand(Bot bot) {
        super("bassboost", Permission.MESSAGE_READ, new String[]{"bb", "bass"}, Category.MUSIC, "Enable the bass booster.", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
        this.getSlashOptions().add(new OptionData(OptionType.INTEGER, "value", "The value of the bassbooster."));
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

        EqualizerFactory equalizer = new EqualizerFactory();
        for (int i = 0; i < BASS_BOOST.length; i++) {
            equalizer.setGain(i, (float) (BASS_BOOST[i] + (input / 1000)));
        }
        guildMusicManager.player.setFilterFactory(equalizer);
        guildMusicManager.player.getPlayingTrack().setPosition(guildMusicManager.player.getPlayingTrack().getPosition());

        e.reply("Bass boost set to " + input).queue();
    }
}
