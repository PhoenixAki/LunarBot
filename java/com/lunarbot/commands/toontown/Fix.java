package com.lunarbot.commands.toontown;

/*
    * LunarBot v2.3.1 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Fix
    * FIX GAM
    * Takes in format !fix
*/

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.TOONTOWN;

public class Fix extends Command {
    public Fix(){
        super(TOONTOWN, "fix` - FIX GAM", "fix` - RELEAS GAM", "Fix");
    }

    public void action(String[] args, MessageReceivedEvent event){
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        event.getChannel().sendMessage("**F I X G A M\nF I X G A M\nF I X G A M**").queue();
    }
}