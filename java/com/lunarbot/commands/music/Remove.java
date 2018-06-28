package com.lunarbot.commands.music;

/*
    * LunarBot v2.0 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Remove
    * Removes a track from the queue, based on its position in the queue.
    * Takes in format !remove songNumber
 */

import com.lunarbot.commands.Command;

import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.MUSIC;

public class Remove extends Command {
    public Remove(){
        super(MUSIC, "remove` - Removes a song from the queue.", "remove songNumber`: Removes a song from the queue.", "Remove");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }
        int trackNumber;

        //Ensures LunarBot is connected to voice before continuing
        if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
            return;
        }

        if(args.length > 0){
            try{
                trackNumber = Integer.parseInt(args[0])-1; //-1 to adjust for array index
            }catch(NumberFormatException exception){
                event.getChannel().sendMessage("Invalid argument (songNumber must be a number). Type `" + Main.prefix + "remove help` for more info.").queue();
                return;
            }

            Main.scheduler.removeTrack(trackNumber, event);
        }else{
            event.getChannel().sendMessage("Invalid format! Type `" + Main.prefix + "remove help` for more info.").queue();
        }
    }
}
