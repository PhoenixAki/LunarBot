package com.lunarbot.commands.music;

/*
    * LunarBot v1.0 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Skip
    * Skips the current track and loads the next track in the queue.
    * Takes in format !skip
 */

import com.lunarbot.commands.Command;

import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.MUSIC;

public class Skip extends Command {
    public Skip(){
        super(MUSIC, "`skip` - Skips the current song.", "`skip`: Skips the current song.", "Skip");
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
            Main.scheduler.nextTrack();
        }
    }
}
