package com.lunarbot.commands.toontown;

/*
 * LunarBot v1.2 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
 *
 * Group
 * Coordinates groups.
 * Takes in format !group list, !group add titleHere sizeHere, !group update titleHere countHere, !group remove titleHere
 */

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.TOONTOWN;

public class Group extends Command {
    public Group(){
        super(TOONTOWN, "group` - Coordinates groups.", "group list` - Lists all currently open groups.\n" + Main.prefix + "group add sizeHere titleHere` - Adds a new group with given title and how many people are wanted.\n"
                + Main.prefix + "group update titleHere countHere` - Updates a pre-existing group's player count.\n" + Main.prefix + "group remove titleHere` - Removes the given group from the list.", "Group");
    }

    public void action(String[] args, MessageReceivedEvent event){
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }


    }
}