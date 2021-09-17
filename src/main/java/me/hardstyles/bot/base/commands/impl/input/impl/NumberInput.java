package me.hardstyles.bot.base.commands.impl.input.impl;

import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.commands.impl.input.Input;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class NumberInput extends Input {

    private final CommandContext cxt;
    private final int position;

    public NumberInput(final CommandContext cxt, final int position) {
        super(cxt, position);
        this.cxt = cxt;
        this.position = position;
    }

    public double value(String name) {
        if (this.isSet(name)) {
            if (cxt.isSlash()) {
                if (cxt.getSlashEvent().getOption(name).getType() == OptionType.NUMBER || cxt.getSlashEvent().getOption(name).getType() == OptionType.INTEGER) {
                    return cxt.getSlashEvent().getOption(name).getAsLong();
                }
            } else {
                try {
                    return Double.parseDouble(cxt.getArgs()[position - 1]);
                } catch (Exception e) {
                    return -9999;
                }
            }
        }
        return -9999;

    }


}
