package me.hardstyles.bot.base.commands;


import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.var;
import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.commands.impl.Category;
import me.hardstyles.bot.base.commands.impl.Command;

import java.lang.reflect.Constructor;
import java.util.HashSet;

@Getter
public class CommandManager {
    private final Bot bot;

    private final HashSet<Command> commands;

    public CommandManager(Bot bot) {
        this.bot = bot;
        this.commands = new HashSet<>();
    }

    @SneakyThrows
    public void initialize() {
        Class<?> type = Bot.class;
        ScanResult result = new ClassGraph().acceptPackages("me.hardstyles.bot.musicbot.commands").scan();
        for (ClassInfo cls : result.getAllClasses()) {
            Class<?> loadClass = cls.loadClass();
            Constructor<?> cons = loadClass.getConstructor(type);
            cons.newInstance(bot);
        }
    }

    public final HashSet<Command> get(Category c) {
        HashSet<Command> cmd = new HashSet<>();
        for (Command command : commands) {
            if (command.getCategory() == c) {
                cmd.add(command);
            }
        }
        return cmd;
    }

    public void register(Command command) {
        this.commands.add(command);
    }
}
