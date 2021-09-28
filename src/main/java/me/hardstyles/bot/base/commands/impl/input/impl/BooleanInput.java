package me.hardstyles.bot.base.commands.impl.input.impl;

import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.commands.impl.input.Input;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class BooleanInput extends Input {

    private final CommandContext cxt;
    private final int position;

    public BooleanInput(final CommandContext cxt, final int position) {
        super(cxt, position);
        this.cxt = cxt;
        this.position = position;
    }

    public boolean value(String name) {
        System.out.println("isSet: " + this.isSet(name));
        if (this.isSet(name)) {
            if (cxt.isSlash()) {
                if (cxt.getSlashEvent().getOption(name).getType() == OptionType.BOOLEAN) {
                    return cxt.getSlashEvent().getOption(name).getAsBoolean();
                }
            } else {
                try {
                    return Boolean.parseBoolean(cxt.getArgs()[position - 1]);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;

    }


}
