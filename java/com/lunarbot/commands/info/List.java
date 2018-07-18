package com.lunarbot.commands.info;

/*
 	* LunarBot v2.3.1 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
 	* 
 	* List
 	* Returns a list of all commands for the given category (listed in the enum Category).
 	* Takes in format !list info/music/toontown
 */

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.SortedSet;
import java.util.TreeSet;

import static com.lunarbot.commands.Category.INFO;

public class List extends Command {
	public List(){
		super(INFO, "list` - Returns list of commands.", "list info/music/toontown`: Displays a list of commands, by category.", "List");
	}

	public void action(String[] args, MessageReceivedEvent event) {
		Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
		if(checkHelp(args, event)) { return; }
		String output = "";

		if(args.length > 0){
		    if(!Main.categories.contains(args[0].toUpperCase())){
		        event.getChannel().sendMessage("Invalid category! Type `" + Main.prefix + "list help` for more info.").queue();
            }else{
		        SortedSet<String> keys = new TreeSet<>(Main.commands.keySet());
		        for(String key : keys){
		            if(Main.commands.get(key).getCategory().name().equalsIgnoreCase(args[0])){
		                output = output.concat("`" + Main.prefix + Main.commands.get(key).getInfo() + "\n");
                    }
                }
                embedOutput(output, event);
            }
        }else{
		    event.getChannel().sendMessage("Invalid format! Type `" + Main.prefix + "list help` for more info.").queue();
        }
	}

	private void embedOutput(String output, MessageReceivedEvent event){
		EmbedBuilder embedBuilder = setupEmbed(event);
		embedBuilder.addField("Commands:\n", output.trim(), false);
		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}
}