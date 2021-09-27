package me.hardstyles.bot.base.commands.impl;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.checkerframework.checker.nullness.Opt;

import java.util.HashSet;

@Getter
@Setter
public class Command {

    private String name;
    private Permission requiredPermission;
    private String[] aliases;
    private Category category;
    private String description;
    private HashSet<OptionData> slashOptions;

    public Command(String name, Permission requiredPermission, String[] aliases, Category category, String description, HashSet<OptionData> options) {
        this.name = name;
        this.requiredPermission = requiredPermission;
        this.aliases = aliases;
        this.category = category;
        this.description = description;
        this.slashOptions = options == null ? new HashSet<>() : options;

    }

    public HashSet<CommandData> toCommand() {
        HashSet<CommandData> hash = new HashSet<>();
        if (aliases != null && aliases.length > 0) {
            for (String alias : aliases) {
                hash.add(slashOptions.isEmpty() ? new CommandData(alias, description) : new CommandData(alias, description).addOptions(slashOptions));
            }
        }
        hash.add(new CommandData(name, description));
        return hash;
    }


    public void execute(CommandContext ctx) {
    }

}
