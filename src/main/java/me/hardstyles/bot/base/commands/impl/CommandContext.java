package me.hardstyles.bot.base.commands.impl;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

@Getter
@Setter
public class CommandContext {

    private final Guild guild;
    private final TextChannel textChannel;
    private final JDA jda;
    private final User user;
    private final Member member;
    private boolean isSlash;
    private SlashCommandEvent slashEvent;
    private Message message;
    private String[] args = new String[]{};

    public CommandContext(Guild guild, TextChannel textChannel, JDA jda, User user, Member member) {
        this.guild = guild;
        this.textChannel = textChannel;
        this.jda = jda;
        this.user = user;
        this.member = member;
        this.isSlash = false;
        this.slashEvent = null;

    }

    public static CommandContext fromSlash(final @NotNull SlashCommandEvent e) {
        CommandContext ctx = new CommandContext(e.getGuild(), e.getTextChannel(), e.getJDA(), e.getUser(), e.getMember());
        ctx.slashEvent = e;
        ctx.isSlash = true;
        return ctx;
    }

    public static CommandContext fromMessage(final @NotNull GuildMessageReceivedEvent e) {
        return new CommandContext(e.getGuild(), e.getMessage().getTextChannel(), e.getJDA(), e.getAuthor(), e.getMember());
    }


    public RestAction<?> reply(String a) {
        if (this.isSlash) {
            return this.slashEvent.reply(a).allowedMentions(Collections.emptySet());
        } else {
            return this.textChannel.sendMessage(a).allowedMentions(Collections.emptySet());
        }
    }

    public RestAction<?> reply(Message a) {
        if (this.isSlash) {
            return this.slashEvent.reply(a).allowedMentions(Collections.emptySet());
        } else {
            return this.textChannel.sendMessage(a).allowedMentions(Collections.emptySet());
        }
    }

    public RestAction<?> reply(MessageEmbed a) {
        if (this.isSlash) {
            return this.slashEvent.replyEmbeds(a);
        } else {
            return this.textChannel.sendMessageEmbeds(a);
        }
    }


    public boolean hasInput(String input) {
        if (isSlash) {
            for (OptionMapping option : slashEvent.getOptions()) {
                if (option.getName().equalsIgnoreCase(input)) {
                    if (slashEvent.getOption(input) != null) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public String textInput(String optionName) {
        if (isSlash) {
            for (OptionMapping option : slashEvent.getOptions()) {
                if (option.getName().equalsIgnoreCase(optionName)) {
                    if (slashEvent.getOption(optionName) == null) {
                        return null;
                    }
                    return slashEvent.getOption(optionName).getAsString();
                }
            }

        }
        return null;
    }

    public float intInput(String optionName) {
        if (isSlash) {
            for (OptionMapping option : slashEvent.getOptions()) {
                if (option.getName().equalsIgnoreCase(optionName)) {
                    if (slashEvent.getOption(optionName) == null) {
                        return -1;
                    }
                    return (int) slashEvent.getOption(optionName).getAsLong();
                }
            }

        }
        return -1;
    }

    public String fullArgs() {
        StringBuilder builder = new StringBuilder(getArgs()[0]);
        for (int i = 1; i < args.length; i++) {
            builder.append(" ").append(getArgs()[i]);
        }
        return builder.toString();
    }

    public String ArgsOrOption(String optionName) {
        if (!isSlash) {
            return fullArgs();
        }
        for (OptionMapping option : slashEvent.getOptions()) {
            if (option.getName().equalsIgnoreCase(optionName)) {
                if (slashEvent.getOption(optionName) == null) {
                    return null;
                }
                return slashEvent.getOption(optionName).getAsString();
            }
        }
        return null;

    }

}
