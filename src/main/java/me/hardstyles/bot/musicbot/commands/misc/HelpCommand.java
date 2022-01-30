package me.hardstyles.bot.musicbot.commands.misc;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unused")

public class HelpCommand extends Command {
    private final Bot bot;

    public HelpCommand(Bot bot) {
        super("help", Permission.MESSAGE_READ, null, Category.OTHER, "a simple help command.", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
        this.getSlashOptions().add(new OptionData(OptionType.STRING, "command", "The command to get more information about").setRequired(true));

    }

    @Override
    public void execute(CommandContext e) {

        if (e.getArgs().length == 0) {
            this.defaultHelp(e);
            return;
        }

        String commandName = e.ArgsOrOption("command");

        Command target = bot.getCommandManager().getCommand(commandName);

        if (target == null) {
            e.reply("Couldn't find that command!").queue();
            return;
        }
        EmbedBuilder builder = bot.getEmbedFactory().coloredEmbed(e.getGuild());
        builder.setTitle("Command info: " + target.getName());
        builder.addField("Description:", target.getDescription(), false);
        builder.addField("Aliases: ", "`" + StringUtils.join(target.getAliases(), "`, `") + "`", true);
        builder.addField("Required Permission:", "`" + target.getRequiredPermission().getName() + "`", true);
        StringBuilder b = new StringBuilder();
        for (OptionData slashOption : target.getSlashOptions()) {
            b.append("\n`").append(slashOption.getName()).append("`: ").append(slashOption.getDescription()).append(" (Type: `").append(slashOption.getType()).append(")`");
        }
        builder.addField("Arguments:", b.length() > 0 ? b.toString() : "N/A", false);
        e.reply(builder.build()).queue();


    }

    private void defaultHelp(CommandContext e) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Category value : Category.values()) {
            stringBuilder.append("\n\n**").append(value.name()).append("**");
            for (Command command : bot.getCommandManager().get(value)) {
                stringBuilder.append("\n`").append(command.getName()).append("` | ").append(command.getDescription()).append(command.getAliases() == null ? "" : " | **Aliases:** `" + StringUtils.join(command.getAliases(), ", ") + "`");
            }
        }
        EmbedBuilder builder = bot.getEmbedFactory().coloredEmbed(e.getGuild());
        builder.setDescription(stringBuilder.toString());
        builder.addField("Invite me to your own server:",String.format("[INVITE ME](https://discord.com/oauth2/authorize?client_id=%s&scope=applications.commands&scope=bot&permissions=8)", e.getJda().getSelfUser().getId()) ,false);
        e.reply(builder.build()).queue();
    }
}
