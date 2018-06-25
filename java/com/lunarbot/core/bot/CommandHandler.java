package com.lunarbot.core.bot;

/*
    * LunarBot v1.0 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * CommandHandler
    * Handles the process of picking up a message, parsing it, and verifying for command existence.
 */

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;

public class CommandHandler extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        //Checks that if the message received is a bot, ignore it and don't increment message count.
        if(event.getAuthor().isBot()){
            return;
        }

        ++Main.messageCount;

        //LunarBot only processes commands if they start with ! or an @mention to its name
        if(event.getMessage().getContentDisplay().startsWith("!")) {
            String message = event.getMessage().getContentDisplay().replaceFirst(Main.prefix, "");
            handleCommand(message.split("\\s+"), event);
        }else if(event.getMessage().getContentDisplay().startsWith("@LunarBot") && event.getMessage().getContentDisplay().indexOf(' ') > 0){
            String message = event.getMessage().getContentDisplay().substring(event.getMessage().getContentDisplay().indexOf(' ')).trim();
            handleCommand(message.split("\\s+"), event);
        }
    }

    private static void handleCommand(String[] args, MessageReceivedEvent event){
        //In the event that this message came via DM, check if it is one of the allowed ones
        //More efficient to confirm here if the command in question is allowed in PMs vs. separately in each of the invalid commands
        if(!event.getChannelType().isGuild()){
            event.getChannel().sendMessage("Sorry, I can't do commands via Direct Messages.").queue();
            return;
        }

        //Checks that the command list has a command associated with the parsed message.
        //If successful, calls action() of the corresponding command.
        boolean safe = false;
        String saveKey = "";

        for(String key : Main.commands.keySet()){
            if(key.equalsIgnoreCase(args[0].toLowerCase())){
                saveKey = key;
                safe = true;
                break;
            }
        }

        if(safe){
            ++Main.commandCount;
            Main.commands.get(saveKey).action(Arrays.copyOfRange(args, 1, args.length), event);
        }else{
            event.getChannel().sendMessage("Invalid command name! Type `" + Main.prefix + "help commands` for a list of commands.").queue();
        }
    }
}
