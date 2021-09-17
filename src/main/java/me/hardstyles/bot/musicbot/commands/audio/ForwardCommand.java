package me.hardstyles.bot.musicbot.commands.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.commands.impl.input.impl.NumberInput;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ForwardCommand extends Command {
    private final Bot bot;

    public ForwardCommand(Bot bot) {
        super("forward", Permission.MESSAGE_READ,new String[]{"skipto"}, Category.MUSIC,"Forward into the track. e.x /forward 20 skips 20s", null);
        this.getOptions().add(new OptionData(OptionType.INTEGER, "value", "The seconds to jump to"));
        this.bot = bot;
        this.bot.getCommandManager().register(this);

    }

    @Override
    public void execute(CommandContext e) {

        if (bot.getVoiceCheck().voiceCheck(e.getMember())) {
            e.reply(bot.getEmbedFactory().error(e, "You're not in the same channel as me, so you can't execute this.").build()).queue();
            return;
        }
        GuildMusicManager guildMusicManager = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());

        NumberInput numberInput = new NumberInput(e, 1);
        double input = numberInput.value("value");

        if (input == -9999 || input == 0) {
            guildMusicManager.player.setFilterFactory(null);
            e.reply("Please give a valid amount of seconds to skip to!").queue();
            return;
        }
        AudioTrack track = guildMusicManager.player.getPlayingTrack();
        if(track == null){
            e.reply("There's no track playing.").queue();
            return;
        }
        if(track.getDuration() < input * 1000){
            e.reply("Given value is longer then the track itself.").queue();
            return;
        }
        guildMusicManager.player.getPlayingTrack().setPosition((long) input * 1000);
        e.reply("Succesfully set the position of the player to " + input).queue();

    }

}
