package me.hardstyles.bot.musicbot.commands.admin;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Icon;

import java.net.URL;
@SuppressWarnings("unused")
public class AvatarCommand extends Command {
    private final Bot bot;

    public AvatarCommand(Bot bot) {
        super("avatar", Permission.ADMINISTRATOR, null, Category.ADMIN, "Change my avatar to a given URL", null);
        this.bot = bot;
        this.bot.getCommandManager().register(this);
    }

    @Override
    public void execute(CommandContext e) {
        try {
            if(e.getJda().retrieveApplicationInfo().complete().getOwner() != e.getUser()){
                e.reply("You can't use this! Only the bot owner can").queue();
                return;
            }
            e.getJda().getSelfUser().getManager().setAvatar(Icon.from(new URL(e.getArgs()[0]).openStream())).queue();
            e.reply("Avatar changed!").queue();
        } catch (Exception exception) {
            e.reply("Couldn't change my avatar.").queue();
        }
    }


}
