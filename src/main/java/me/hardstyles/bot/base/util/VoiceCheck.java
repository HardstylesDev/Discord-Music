package me.hardstyles.bot.base.util;

import me.hardstyles.bot.Bot;
import me.hardstyles.bot.base.audio.GuildMusicManager;
import net.dv8tion.jda.api.entities.Member;

public class VoiceCheck {
    public boolean voiceCheck(Member member) {
        if (member == null || member.getVoiceState() == null || member.getVoiceState().getChannel() == null)
            return true;
        else if (member.getGuild().getSelfMember().getVoiceState().getChannel() == null)
            return true;
        else if (member.getVoiceState().getChannel() == member.getGuild().getSelfMember().getVoiceState().getChannel())
            return false;
        return true;
    }

    public boolean isPlaying(Member member, Bot bot) {
        GuildMusicManager b = bot.getAudioHandler().getMusicManagers().get(member.getGuild().getIdLong());
        return (b == null || b.player == null || b.player.getPlayingTrack() == null || b.player.getPlayingTrack() == null);
    }
}
