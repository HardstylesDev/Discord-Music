package me.hardstyles.bot.base.commands;


import lombok.Getter;
import me.hardstyles.bot.Bot;
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
    public void register(Command command){
        this.commands.add(command);
    }

}
