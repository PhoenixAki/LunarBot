package com.lunarbot.commands.info;

/*
 	* LunarBot v1.2 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
 	* 
 	* Shutdown
 	* Shuts down LunarBot.
 	* Takes in format !shutdown
 */

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.INFO;

public class Shutdown extends Command {
	public Shutdown(){
		super(INFO, "shutdown` - Shuts down LunarBot.", "shutdown`: Shuts down LunarBot.", "Shutdown");
	}

	public void action(String[] args, MessageReceivedEvent event) {
		Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
		if(checkHelp(args, event)) { return; }

		if(event.getAuthor().getId().equalsIgnoreCase("85603224790265856")){
			event.getChannel().sendMessage(":wave: Shutting down.").complete();
			event.getJDA().shutdown();
		}else{
			event.getChannel().sendMessage("Sorry, this command is accessible to my developer only.").queue();
		}
	}
}
