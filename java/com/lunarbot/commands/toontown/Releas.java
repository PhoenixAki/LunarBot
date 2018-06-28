package com.lunarbot.commands.toontown;

/*
    * LunarBot v1.3 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Releas
    * RELEAS GAM
    * Takes in format !releas
*/

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.TOONTOWN;

public class Releas extends Command {
    public Releas(){
        super(TOONTOWN, "releas` - RELEAS GAM", "releas` - RELEAS GAM", "Releas");
    }

    public void action(String[] args, MessageReceivedEvent event){
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        event.getChannel().sendMessage("https://www.youtube.com/watch?v=dlpp51eUpCI").queue();
        event.getChannel().sendMessage("**R E L E A S G A M\nR E L E A S G A M\nR E L E A S G A M**").queue();
    }
}