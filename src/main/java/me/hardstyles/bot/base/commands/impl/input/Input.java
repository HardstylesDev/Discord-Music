package me.hardstyles.bot.base.commands.impl.input;

import me.hardstyles.bot.base.commands.impl.CommandContext;
import org.jetbrains.annotations.NotNull;

public class Input {
    private final CommandContext cxt;
    private final int position;

    public Input(@NotNull final CommandContext cxt, final int position) {
        this.position = position;
        this.cxt = cxt;
    }

    protected boolean isSet(String name) {
        if (cxt.isSlash()) {
            if (cxt.getSlashEvent().getOptionsByName(name).isEmpty()) {
                return false;
            }
            return cxt.getSlashEvent().getOption(name) != null;
        } else {
            if (cxt.getArgs().length == 0) return false;
            if(cxt.getArgs().length < position) return false;
            if (cxt.getArgs()[position - 1] != null) {
                return true;
            }
        }
        return true;
    }

}
