package me.hardstyles.bot.musicbot.commands.misc;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;
@SuppressWarnings("unused")

public class PingCommand extends Command {
    private final Bot bot;
    public PingCommand(Bot bot){
        super("ping", Permission.MESSAGE_READ, null, Category.OTHER, "Shows my ping", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e){
        e.reply("My current ping is `" + e.getJda().getGatewayPing() + "`").queue();

    }


}