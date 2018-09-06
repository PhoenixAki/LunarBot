package com.lunarbot.core.audio;

/*
    * LunarBot v2.4.1 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * TrackInfo
    * Used in setting user data for Audio Tracks. Contains username of requester and the channel associated to it.
 */

import net.dv8tion.jda.core.entities.MessageChannel;

public class TrackInfo {
    private MessageChannel channel;
    private String requester;

    public TrackInfo(String requester, MessageChannel channel){
        this.requester = requester;
        this.channel = channel;
    }

    public String getRequester(){
        return requester;
    }

    public MessageChannel getChannel(){
        return channel;
    }
}
