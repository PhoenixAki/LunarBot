package com.lunarbot.commands.info;

/*
    * LunarBot v2.3.1 by PhoenixAki: General purpose bot for usage in the Lunar Draconis clan server.
    *
    * Status
    * Outputs information about the bot's current status.
    * Takes in format !status
 */

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.INFO;

public class Status extends Command {
    public Status(){
        super(INFO, "status` - Outputs info about LunarBot's current status.", "status`: Outputs info about LunarBot's current status.", "Status");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        embedOutput(event);
    }

    private void embedOutput(MessageReceivedEvent event){
        EmbedBuilder embedBuilder = setupEmbed(event);

        long elapsedTime = System.currentTimeMillis() + 14400000 - Main.startupTime;
        String hours = Long.toString(elapsedTime/3600000), minutes = Long.toString((elapsedTime/60000)%60), seconds = Long.toString((elapsedTime/1000)%60);

        //Padding for output, if necessary.
        hours = (Integer.parseInt(hours) < 10) ? "0" + hours : hours;
        minutes = (Integer.parseInt(minutes) < 10) ? "0" + minutes : minutes;
        seconds = (Integer.parseInt(seconds) < 10) ? "0" + seconds : seconds;

        embedBuilder.addField("Ping", Long.toString(event.getJDA().getPing()) + "ms", true);
        embedBuilder.addField("Uptime", hours + ":" + minutes + ":" + seconds, true);
        embedBuilder.addField("Messages Processed", Long.toString(Main.messageCount), true);
        embedBuilder.addField("Commands Processed", Long.toString(Main.commandCount), true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}