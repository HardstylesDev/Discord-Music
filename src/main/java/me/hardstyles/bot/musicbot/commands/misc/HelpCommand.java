package me.hardstyles.bot.musicbot.commands.misc;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.apache.commons.lang3.StringUtils;
@SuppressWarnings("unused")

public class HelpCommand extends Command {
    private final Bot bot;

    public HelpCommand(Bot bot) {
        super("help", Permission.MESSAGE_READ, null, Category.OTHER, "a simple help command.", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Category value : Category.values()) {
            stringBuilder.append("\n**" + value.name() + "**");
            for (Command command : bot.getCommandManager().get(value)) {
                stringBuilder.append("\n`" + command.getName() + "` | " + command.getDescription() + (command.getAliases() == null ? "" : " | **Aliases:** `" + StringUtils.join(command.getAliases(), ", ") + "`"));
            }
        }
        EmbedBuilder builder = bot.getEmbedFactory().coloredEmbed(e.getGuild());
        builder.setDescription(stringBuilder.toString());
        e.reply(builder.build()).queue();
    }


}
