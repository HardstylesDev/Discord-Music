package me.hardstyles.bot.base.commands.impl.input.impl;

import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.commands.impl.input.Input;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class WordInput extends Input {

    private final CommandContext cxt;
    private final int position;

    public WordInput(final CommandContext cxt, final int position) {
        super(cxt, position);
        this.cxt = cxt;
        this.position = position;
    }

    public String value(String name) {
        if (this.isSet(name)) {
            if (cxt.isSlash()) {
                if (cxt.getSlashEvent().getOption(name).getType() == OptionType.STRING) {
                    return cxt.getSlashEvent().getOption(name).getAsString();
                }
            } else {
                try {
                    return (cxt.getArgs()[position - 1]);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;

    }


}
