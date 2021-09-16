package me.hardstyles.bot.musicbot.commands.admin;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PrefixCommand extends Command {
    private final Bot bot;


    public PrefixCommand(Bot bot) {
        super("prefix", Permission.ADMINISTRATOR, new String[]{"setprefix"}, Category.ADMIN, "Change the prefix in this server", null);
        this.bot = bot;
        this.getOptions().add(new OptionData(OptionType.STRING, "new_prefix", "The new prefix").setRequired(true));
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {

        if (e.getArgs().length == 0 && !e.hasInput("new_prefix")) {
            EmbedBuilder builder = bot.getEmbedFactory().coloredEmbed(e.getGuild());
            builder.setTitle("Prefix for " + e.getGuild().getName());
            builder.setDescription("The current prefix is " + bot.getPrefixManager().getPrefix(e.getGuild()));
            e.reply(builder.build()).queue();
            return;
        }

        String newPrefix = e.isSlash() ? e.textInput("new_prefix") : e.getArgs()[0];
        bot.getPrefixManager().setPrefix(e.getGuild(), newPrefix);
        EmbedBuilder builder = bot.getEmbedFactory().coloredEmbed(e.getGuild());
        builder.setTitle("Prefix updated! ");
        builder.setDescription("The new prefix is `" + newPrefix + "`");
        e.reply(builder.build()).queue();
    }
}