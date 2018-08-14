package com.lunarbot.commands.music;

/*
	* LunarBot v2.4 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
	*
	* Pause
	* Pauses the current track.
	* Takes in format !pause
 */

import com.lunarbot.commands.Command;

import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.MUSIC;

public class Pause extends Command {
    public Pause(){
        super(MUSIC, "pause` - Pauses the current song.", "pause`: Pauses the current song.\nTo resume a paused song, type `" + Main.prefix + "play`.", "Pause");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        //Ensures LunarBot is connected to voice before continuing
        if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
            return;
        }

        if(Main.player.getPlayingTrack() == null){
            event.getChannel().sendMessage("No song playing!").queue();
        }else if(Main.player.isPaused()){
            event.getChannel().sendMessage("Song is already paused! Type `" + Main.prefix + "play` to resume.").queue();
        }else{
            Main.player.setPaused(true);
            event.getChannel().sendMessage("Song paused.").queue();
        }
    }
}
