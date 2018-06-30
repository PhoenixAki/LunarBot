package com.lunarbot.core.audio;

/*
    * LunarBot v2.1.1 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * AudioPlayerSendHandler
    * Handles the process of sending audio to the guild's audio player.
 */

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public boolean canProvide() {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    public byte[] provide20MsAudio() {
        return lastFrame.getData();
    }

    public boolean isOpus() {
        return true;
    }
}
