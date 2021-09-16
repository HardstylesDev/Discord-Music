package me.hardstyles.bot.musicbot.commands.audio;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class BassBoostCommand extends Command {
    private final Bot bot;

    public BassBoostCommand(Bot bot) {
        super("bassboost", Permission.MESSAGE_READ, new String[]{"bb", "bass"}, Category.MUSIC, "Enable the bass booster.", null);
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
        GuildMusicManager guildMusicManager = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());


        if (e.getArgs().length == 0 && !e.hasInput("value")) {
            guildMusicManager.player.setFilterFactory(null);
            e.reply("Bassboost has been disabled!").queue();
            return;
        }

        float value;
        if (e.hasInput("value")) {
            value = e.intInput("value");
        } else {
            value = Float.parseFloat(e.getArgs()[0]);
        }
        if (value == 0) {
            guildMusicManager.player.setFilterFactory(null);
        } else {
            EqualizerFactory equalizer = new EqualizerFactory();
            for (int i = 0; i < BASS_BOOST.length; i++) {
                equalizer.setGain(i, BASS_BOOST[i] + (value / 100));
            }
            guildMusicManager.player.setFilterFactory(equalizer);
        }
        e.reply("Bass boost set to " + value).queue();
    }
}
