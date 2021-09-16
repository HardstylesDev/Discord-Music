package me.hardstyles.bot.base.util;

import net.dv8tion.jda.api.entities.Member;

public class VoiceCheck {
    public boolean voiceCheck(Member member){
        if(member == null || member.getVoiceState() == null || member.getVoiceState().getChannel() == null)
            return true;
        else if(member.getGuild().getSelfMember().getVoiceState().getChannel() == null)
            return true;
        else if (member.getVoiceState().getChannel() == member.getGuild().getSelfMember().getVoiceState().getChannel())
            return false;
        return true;
    }
}
