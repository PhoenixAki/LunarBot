package com.lunarbot.commands.music;

/*
    * LunarBot v1.2 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Song
    * Outputs info about the currently playing track (title, url link, position/duration, and who requested it).
    * Takes in format !song
 */

import com.lunarbot.commands.Command;
import com.lunarbot.core.audio.TrackInfo;
import com.lunarbot.core.bot.Main;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.MUSIC;

public class Song extends Command {
    public Song(){
        super(MUSIC, "song` - Outputs current song info.", "song`: Outputs info about the currently playing song.", "Song");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        if(Main.player.getPlayingTrack() == null){
            event.getChannel().sendMessage("No song is playing!").queue();
        }else{
            embedOutput(Main.player.getPlayingTrack(), event);
        }
    }

    private void embedOutput(AudioTrack track, MessageReceivedEvent event){
        EmbedBuilder embedBuilder = setupEmbed(event);

        Long duration = track.getDuration(), currentTime = track.getPosition();
        String durationMinutes = Long.toString((duration/60000)%60), durationSeconds = Long.toString((duration/1000)%60);
        String currentMinutes = Long.toString((currentTime/60000)%60), currentSeconds = Long.toString((currentTime/1000)%60);

        //Padding for output, if necessary
        durationMinutes = Integer.parseInt(durationMinutes) < 10 ? "0" + durationMinutes : durationMinutes;
        durationSeconds = Integer.parseInt(durationSeconds) < 10 ? "0" + durationSeconds : durationSeconds;
        currentMinutes = Integer.parseInt(currentMinutes) < 10 ? "0" + currentMinutes : currentMinutes;
        currentSeconds = Integer.parseInt(currentSeconds) < 10 ? "0" + currentSeconds : currentSeconds;

        embedBuilder.addField("Song Title:", track.getInfo().title, false);
        embedBuilder.addField("Song Link:", track.getInfo().uri, false);
        embedBuilder.addField("Current Position:", currentMinutes + ":" + currentSeconds + "/" + durationMinutes + ":" + durationSeconds, false);
        embedBuilder.addField("Requested by:", ((TrackInfo)track.getUserData()).getRequester(), false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}