package com.lunarbot.core.audio;

/*
    * LunarBot v2.4.1 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * TrackScheduler
    * Handles all track scheduling actions.
 */

import com.lunarbot.commands.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    public final BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        player.setVolume(50);
        this.queue = new LinkedBlockingQueue<>();
    }

    public void addTrack(AudioTrack track){
        if(!player.startTrack(track, true)){
            queue.offer(track);
        }
    }

    public void addPlaylist(AudioPlaylist playlist, MessageReceivedEvent event){
        for(AudioTrack track : playlist.getTracks()){
            if(!player.startTrack(track, true)){
                queue.offer(track);
            }
            track.setUserData(new TrackInfo(event.getMember().getEffectiveName(), event.getChannel()));
        }
    }

    public void nextTrack(){
        //Check if there is a next track, and just return out if not.
        if(queue.peek() == null){
            ((TrackInfo)player.getPlayingTrack().getUserData()).getChannel().sendMessage("End of playback queue reached.").queue();
            player.stopTrack();
            return;
        }

        TrackInfo info = (TrackInfo)queue.peek().getUserData();
        player.startTrack(queue.poll(), false);
        info.getChannel().sendMessage(":musical_note: Now Playing: **" + player.getPlayingTrack().getInfo().title + "**, requested by **" + info.getRequester() + "**").queue();
    }

    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            nextTrack();
        }
    }

    public void outputQueue(MessageReceivedEvent event){
        EmbedBuilder embedBuilder = Command.setupEmbed(event);
        int songCount = 0;

        //Checks if the queue is empty before continuing.
        if(queue.peek() == null){
            event.getChannel().sendMessage("Queue is empty!").queue();
            return;
        }

        //Loops through the queue until (at the latest) song #10 is processed, then stops.
        for(AudioTrack track : queue){
            ++songCount;
            TrackInfo currentTrackInfo = (TrackInfo)track.getUserData();
            String title = track.getInfo().title;
            embedBuilder.addField("Song " + songCount, "**Title:** " + title + "\n**Requested by:** " + currentTrackInfo.getRequester(), false);
            if(songCount == 10){
                break;
            }
        }

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public void removeTrack(int trackNumber, MessageReceivedEvent event){
        //Loops through the queue until the correct track is found, and removes it.
        int i = 0;

        if(trackNumber >= queue.size() || trackNumber < 0){
            event.getChannel().sendMessage("Invalid `songNumber` - there are " + queue.size() + " song(s) queued. Enter a number in range and try again.").queue();
            return;
        }

        for(AudioTrack track : queue){
            if(i == trackNumber){
                queue.remove(track);
                event.getChannel().sendMessage("**" + track.getInfo().title + "** removed from Queue.").queue();
                break;
            }else{
                ++i;
            }
        }
    }

    public void clearQueue(MessageReceivedEvent event){
        queue.clear();
        if(event != null){
            event.getChannel().sendMessage("Queue emptied.").queue();
        }
    }

    public boolean hasNextTrack(){
        return queue.peek() != null;
    }
}