package com.lunarbot.commands.music;

/*
    * LunarBot v2.5 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Seek
    * Skips to the given time in the playing track.
    * Takes in format !seek mm:ss
*/

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.regex.Pattern;

import static com.lunarbot.commands.Category.MUSIC;

public class Seek extends Command {
    public Seek(){
        super(MUSIC, "seek` - Skips to the given time in the playing song.", "seek mm:ss`: Skips to the specified position in the song.\nIf the given position is less than 00:00, the song restarts.\nIf the given position is after the end of the song, the song is skipped.", "Seek");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }
        final Pattern POSITION_PATTERN = Pattern.compile("([0-9]+:[0-9]+)");
        Long minutes, seconds;

        //Ensures LunarBot is connected to voice before continuing
        if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
            return;
        }

        if(args.length > 0) {
            //Ensures there is a song playing before trying to seek
            if (Main.player.getPlayingTrack() == null) {
                event.getChannel().sendMessage("No song is playing!").queue();
                return;
            }

            //Match the input with the valid pattern for positions (mm:ss)
            if (!POSITION_PATTERN.matcher(args[0]).find()) {
                event.getChannel().sendMessage("Invalid format! Type `" + Main.prefix + "seek help` for more info.").queue();
                return;
            }

            minutes = Long.parseLong(args[0].substring(0, 2)) * 60000;
            seconds = Long.parseLong(args[0].substring(3)) * 1000;
            Main.player.getPlayingTrack().setPosition(minutes + seconds);
        }else{
            event.getChannel().sendMessage("Invalid format! Type `"  + Main.prefix + "seek help` for more info.").queue();
        }
    }
}