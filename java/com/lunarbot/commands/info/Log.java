package com.lunarbot.commands.info;

/*
 	* LunarBot v2.1 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
 	* 
 	* Log
 	* Only works when I call it; PMs me the log (useful if I'm away from PC but want the log).
 	* Takes in format !log
 */

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.INFO;

public class Log extends Command {
	public Log(){
		super(INFO, "log` - PMs log of activity since startup.", "log`: PMs log of activity since startup.", "Log");
	}

	public void action(String[] args, MessageReceivedEvent event) {
		Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
		if(checkHelp(args, event)) { return; }

		if(event.getAuthor().getId().equalsIgnoreCase("85603224790265856")){
			event.getAuthor().openPrivateChannel().queue(m -> m.sendFile(Main.log).queue());
		}else{
		    event.getChannel().sendMessage("Sorry, this command is accessible to my developer only.").queue();
        }
	}
}