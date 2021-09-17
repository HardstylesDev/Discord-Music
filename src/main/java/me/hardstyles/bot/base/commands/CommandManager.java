package me.hardstyles.bot.base.commands;


import lombok.Getter;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;

import java.util.HashSet;

@Getter
public class CommandManager {
    private final Bot bot;

    private final HashSet<Command> commands;
    public CommandManager(Bot bot){
        this.bot = bot;
        this.commands = new HashSet<>();




    }

    public final HashSet<Command> get(Category c){
        HashSet<Command> cmd = new HashSet<>();
        for (Command command : commands) {
            if(command.getCategory() == c){
                cmd.add(command);
            }
        }
        return cmd;
    }
    public void register(Command command){
        this.commands.add(command);
    }

}
