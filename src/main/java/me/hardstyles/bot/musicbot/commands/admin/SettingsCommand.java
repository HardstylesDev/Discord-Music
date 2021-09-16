package me.hardstyles.bot.musicbot.commands.admin;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.guild.ChatType;
import me.hardstyles.bot.base.guild.Guild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SettingsCommand extends Command {
    private final Bot bot;


    public SettingsCommand(Bot bot) {
        super("settings", Permission.ADMINISTRATOR, new String[]{"config", "options"}, Category.ADMIN, "Change settings in this server", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);

        this.getOptions().add(new OptionData(OptionType.STRING, "chat_type", "The chat type you'd like to use").addChoice("Default", "default").addChoice("Simple", "simple").addChoice("Emote", "reaction").setRequired(true));


    }


    @Override
    public void execute(CommandContext e) {

        Guild guild = bot.getGuildManager().getGuildFromId(e.getGuild().getId());
        if (e.getArgs().length == 0 && !e.hasInput("chat_type")) {
            EmbedBuilder builder = bot.getEmbedFactory().coloredEmbed(e.getGuild());
            builder.setTitle("Settings" + e.getGuild().getName());
            builder.setDescription("The current chattype is: `" + guild.getChatType() + "`");
            e.reply(builder.build()).queue();
            return;
        }

        String type = e.isSlash() ? e.textInput("chat_type") : e.getArgs()[0];
        EmbedBuilder builder = bot.getEmbedFactory().coloredEmbed(e.getGuild());
        builder.setTitle("Chat Type updated! ");
        builder.setDescription("The new ChatType has been set to `" + type + "`");
        guild.getGuildJson().addProperty("ChatType", ChatType.valueOf(type.toUpperCase()) + "");
        bot.getGuildManager().update(e.getGuild().getId(), guild.getGuildJson());
        e.reply(builder.build()).queue();
    }
}