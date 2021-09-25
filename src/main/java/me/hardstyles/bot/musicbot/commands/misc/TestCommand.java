package me.hardstyles.bot.musicbot.commands.misc;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.guild.GuildObj;
import net.dv8tion.jda.api.Permission;

public class TestCommand extends Command {
    private final Bot bot;

    public TestCommand(Bot bot) {
        super("test", Permission.ADMINISTRATOR, null, Category.ADMIN, "a simple test command.", null);
        this.bot = bot;
        bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {
        e.reply("test").queue();
    }
}
