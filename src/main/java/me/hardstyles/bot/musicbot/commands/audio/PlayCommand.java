package me.hardstyles.bot.musicbot.commands.audio;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.ArrayList;
@SuppressWarnings("unused")

public class PlayCommand extends Command {
    private final Bot bot;

    public PlayCommand(Bot bot) {
        super("play", Permission.MESSAGE_READ, new String[]{"p", "add"}, Category.MUSIC, "Add a song to the queue", null);

        this.getSlashOptions().add(new OptionData(OptionType.STRING, "song", "The URL/name of the song you'd like to play/add to the queue").setRequired(true));

        this.bot = bot;
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {
        String a = e.ArgsOrOption("song");
        if (isUrlValid(a)) {
            if (a.contains("open.spotify")) {
                if (a.contains("spotify.com/playlist")) {
                    String[] urlA = a.split("/");
                    String listId = urlA[urlA.length - 1].split("\\?")[0];

                    ArrayList<String> items = bot.getSpotifyApi().fuck(listId);

                    bot.getAudioHandler().loadPlaylist(e, items.toArray(new String[0]));
                    e.reply(bot.getEmbedFactory().addedPlaylist(e, items.toArray(new String[0])).build()).queue();
                    return;
                }

                String[] urlA = a.split("/");
                String trackID = urlA[urlA.length - 1].split("\\?")[0];
                String search = bot.getSpotifyApi().simpleTrack(trackID);
                bot.getAudioHandler().loadAndPlay(e, "ytsearch: " + search);
                return;
            }

            if (a.contains("soundcloud")) {
                bot.getAudioHandler().loadAndPlay(e, a);
                return;
            }
            if (a.contains("&list")) {
                if (a.contains("&list=LL&") || a.endsWith("&list=LL")) {
                    bot.getAudioHandler().loadAndPlay(e, a.split("&list")[0], true);
                }
            } else
                bot.getAudioHandler().loadAndPlay(e, a, true);
        } else {


            bot.getAudioHandler().loadAndPlay(e, "ytsearch: " + a);
        }
    }

    private boolean isUrlValid(String url) {
        UrlValidator defaultValidator = new UrlValidator();
        return defaultValidator.isValid(url);
    }

}
