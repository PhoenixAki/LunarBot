package com.lunarbot.commands.music;

/*
    * LunarBot v2.3 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Volume
    * Displays the current volume (default is set to 50), or changes to the given value.
    * Takes in format !volume newVolume
 */

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.MUSIC;

public class Volume extends Command {
    public Volume(){
        super(MUSIC, "volume` - Changes the current volume.", "volume`: Displays the current volume.\n`" + Main.prefix + "volume newVolume`: Updates the current volume (`newVolume` must be 1-100).", "Volume");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }
        int newVolume;

        //Ensures LunarBot is connected to voice before continuing
        if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
            return;
        }

        if(args.length == 0){
            event.getChannel().sendMessage("Current volume: " + Main.player.getVolume() + ".").queue();
        }else{
            try{
                newVolume = Integer.parseInt(args[0]);
            }catch(NumberFormatException e){
                event.getChannel().sendMessage("Invalid argument (`newVolume` must be a number). Type `" + Main.prefix + "volume help` for more info.").queue();
                return;
            }

            if(newVolume < 1 || newVolume > 100){
                event.getChannel().sendMessage("Invalid argument (`newVolume` must be 1-100). Type `" + Main.prefix + "volume help` for more info.").queue();
            }else{
                Main.player.setVolume(newVolume);
                event.getChannel().sendMessage("Volume changed to " + newVolume + ".").queue();
            }
        }
    }
}