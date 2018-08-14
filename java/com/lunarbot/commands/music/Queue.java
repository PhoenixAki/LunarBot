package com.lunarbot.commands.music;

/*
    * LunarBot v2.4 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Queue
    * Outputs info about the current queue, up to the first 10 songs (to avoid the embed being too long).
    * Takes in format !queue clear
 */

import com.lunarbot.commands.Command;

import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.MUSIC;

public class Queue extends Command {
    public Queue(){
        super(MUSIC, "queue` - Displays info about songs in the queue.", "queue`: Displays info for (up to) the first 10 songs in the queue.\n`" + Main.prefix + "queue clear`: Clears the queue.", "Queue");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        if(args.length == 0){
            Main.scheduler.outputQueue(event);
        }else if(args[0].equalsIgnoreCase("clear")){
            Main.scheduler.clearQueue(event);
        }else{
            event.getChannel().sendMessage("Invalid format! Type `" + Main.prefix + "queue help` for more info.").queue();
        }
    }
}