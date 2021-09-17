package me.hardstyles.bot.musicbot.event;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import me.hardstyles.bot.base.commands.impl.Command;
import me.hardstyles.bot.base.commands.impl.CommandContext;
import me.hardstyles.bot.base.guild.GuildObj;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class EventListener extends ListenerAdapter {


    private final Bot bot;

    public EventListener(Bot bot) {
        this.bot = bot;
    }

    private boolean isBotMention(GuildMessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        long id = event.getJDA().getSelfUser().getIdLong();
        return content.startsWith("<@" + id + ">") || content.startsWith("<@!" + id + ">");
    }


    @Override
    public final void onGuildMessageReceived(final @NotNull GuildMessageReceivedEvent e) {
        final Guild guild = e.getGuild();
        GuildObj guildObj = bot.getGuildManager().getGuildFromId(guild.getId());

        final String prefix = guildObj.getPrefix();
        final User author = e.getAuthor();
        final Message message = e.getMessage();
        if ((message.getContentRaw().startsWith(prefix) && !author.isBot())) {
            String content;
            if (!message.getMentionedMembers().isEmpty() && message.getContentRaw().split(" ")[0].equalsIgnoreCase(message.getMentionedMembers().get(0).getAsMention())) {
                content = message.getContentRaw().substring(message.getMentionedMembers().get(0).getAsMention().length() );
            } else {
                content = message.getContentRaw().substring(prefix.length());
            }
            final String command = content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content;
            for (Command cmd : this.bot.getCommandManager().getCommands()) {
                if (cmd.getName().equalsIgnoreCase(command)) {
                    if (!e.getMember().hasPermission(cmd.getRequiredPermission())) {
                        e.getMessage().getTextChannel().sendMessage(bot.getEmbedFactory().noPermission(message.getMember(), cmd.getRequiredPermission()).build()).queue();
                        return;
                    }
                    handleCommand(e, content, cmd);
                    break;
                }
                if (cmd.getAliases() != null) {
                    for (String alias : cmd.getAliases()) {
                        if (alias.equalsIgnoreCase(command)) {
                            if (!e.getMember().hasPermission(cmd.getRequiredPermission())) {
                                e.getMessage().getTextChannel().sendMessage(bot.getEmbedFactory().noPermission(message.getMember(), cmd.getRequiredPermission()).build()).queue();
                                return;
                            }
                            handleCommand(e, content, cmd);
                            break;
                        }
                    }
                }
            }
        }

        if (isBotMention(e) && !author.isBot()) {
            message.getTextChannel().sendMessage("My prefix here is `" + prefix + "`").queue();
        }
    }

    private void handleCommand(@NotNull GuildMessageReceivedEvent e, String content, Command cmd) {
        final String[] tmp = content.split("\\s+");
        final String[] args = Arrays.copyOfRange(tmp, 1, tmp.length);
        CommandContext ctx = CommandContext.fromMessage(e);
        ctx.setArgs(args);
        cmd.execute(ctx);
    }

    @Override
    public final void onSlashCommand(final @NotNull SlashCommandEvent event) {

        for (Command command : bot.getCommandManager().getCommands()) {

            if (command.getName().equalsIgnoreCase(event.getName())) {
                handleSlashCommand(event, command);
                break;
            }
            if (command.getAliases() != null) {
                for (String alias : command.getAliases()) {
                    if (alias.equalsIgnoreCase(event.getName())) {
                        handleSlashCommand(event, command);
                    }
                }
            }
        }
    }

    private void handleSlashCommand(@NotNull SlashCommandEvent event, Command command) {
        if (!event.getMember().hasPermission(command.getRequiredPermission())) {
            event.replyEmbeds(bot.getEmbedFactory().noPermission(event.getMember(), command.getRequiredPermission()).build()).queue();
            return;
        }
        CommandContext ctx = CommandContext.fromSlash(event);
        command.execute(ctx);
    }

    @Override
    public final void onReady(ReadyEvent event) {

        System.out.println("Ready event called.");
        CommandListUpdateAction commands = event.getJDA().updateCommands();
        for (Command cmd : bot.getCommandManager().getCommands()) {
            commands.addCommands(cmd.toCommand());
            System.out.println(cmd.getName());
        }
        commands.queue();

    }

    @Override
    public final void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        GuildMusicManager m = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());
        if (m == null || m.player == null || m.scheduler == null || e.getChannelLeft().getMembers().size() < 2) {
            e.getGuild().getAudioManager().closeAudioConnection();
        }
    }

    @Override
    public final void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        GuildMusicManager m = bot.getAudioHandler().getMusicManagers().get(e.getGuild().getIdLong());
        if (m == null || m.player == null || m.scheduler == null || e.getChannelLeft().getMembers().size() < 2) {
            e.getGuild().getAudioManager().closeAudioConnection();
        }
    }

}
