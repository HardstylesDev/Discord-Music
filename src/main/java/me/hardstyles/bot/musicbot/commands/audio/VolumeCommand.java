package me.hardstyles.bot.musicbot.commands.audio;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.commands.impl.input.impl.NumberInput;
import me.hardstyles.bot.base.guild.GuildObj;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
@SuppressWarnings("unused")

public class VolumeCommand extends Command {
    private final Bot bot;

    public VolumeCommand(Bot bot) {
        super("volume", Permission.MESSAGE_READ, new String[]{"setvolume"}, Category.MUSIC, "Set the volume your input (1-100)", null);
        this.getSlashOptions().add(new OptionData(OptionType.INTEGER, "value", "Volume"));
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
            if(guildMusicManager != null && guildMusicManager.player != null) {
                e.reply("The current value is " + guildMusicManager.player.getVolume()).queue();
            }
            return;
        }
        if (input < 1 || input > 100) {
            e.reply("Please provide a valid volume from 1-100!").queue();
            return;
        }

        guildMusicManager.player.setVolume((int) input);
        e.reply("Succesfully changed the volume to " + input).queue();

        GuildObj obj = bot.getGuildManager().getGuildFromId(e.getGuild().getId());
        obj.setVolume(input);
        bot.getGuildSettingsHandler().write();

    }

}
