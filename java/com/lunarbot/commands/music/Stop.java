package com.lunarbot.commands.music;

/*
    * LunarBot v1.4 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Stop
    * Stops the current track.
    * Takes in format !stop
 */

import com.lunarbot.commands.Command;

import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.MUSIC;

public class Stop extends Command {
    public Stop(){
        super(MUSIC, "stop` - Stops the current song.", "stop`: Stops the current song. Stopping and then resuming with `"+ Main.prefix + "play` is effectively the same as calling `" + Main.prefix + "skip`.", "Stop");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        //Ensures LunarBot is connected to voice before continuing
        if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
            return;
        }

        if(Main.player.getPlayingTrack() == null){
            event.getChannel().sendMessage("No song is playing!").queue();
        }else{
            Main.player.stopTrack();
            event.getChannel().sendMessage("Song stopped.").queue();
        }
    }
}