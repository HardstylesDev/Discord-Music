package me.hardstyles.bot.musicbot.commands.audio;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.commons.validator.routines.UrlValidator;

public class PlayCommand extends Command {
    private final Bot bot;

    public PlayCommand(Bot bot) {
        super("play", Permission.MESSAGE_READ, new String[]{"p", "add"}, Category.MUSIC, "Add a song to the queue", null);

        this.getOptions().add(new OptionData(OptionType.STRING, "song", "The URL/name of the song you'd like to play/add to the queue").setRequired(true));

        this.bot = bot;
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {

        String a = e.ArgsOrOption("song");


        System.out.println("We here babes");

        if (isUrlValid(a)) {
            if (a.contains("&list")) {
                if (a.contains("&list=LL&") || a.endsWith("&list=LL")) {
                    bot.getAudioHandler().loadAndPlay(e, a.split("&list")[0], true);
                }
            } else
                bot.getAudioHandler().loadAndPlay(e, a, true);
            System.out.println("st");
        } else {

            bot.getAudioHandler().loadAndPlay(e, "ytsearch: " + a);
            System.out.println("raw");
        }
    }

    private boolean isUrlValid(String url) {
        UrlValidator defaultValidator = new UrlValidator();
        return defaultValidator.isValid(url);
    }

}