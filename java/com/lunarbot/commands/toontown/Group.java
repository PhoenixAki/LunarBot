package com.lunarbot.commands.toontown;

/*
 * LunarBot v2.3 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
 *
 * Group
 * Coordinates groups.
 * Takes in formats !group list, !group join groupNumber, !group add sizeHere titleHere, !group remove groupNumber, !group leave groupNumber
 */

import com.lunarbot.commands.Command;
import com.lunarbot.commands.GameGroup;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

import static com.lunarbot.commands.Category.TOONTOWN;

public class Group extends Command {
    private ArrayList<GameGroup> groups = new ArrayList<>();

    public Group(){
        super(TOONTOWN, "group` - Coordinates groups.", "group list` - Lists all currently open groups.\n\n`"
                + Main.prefix + "group join groupNumber` - Joins a group.\n\n`" + Main.prefix + "group add sizeHere titleHere` - Adds a new group with given title and how many people are wanted.\n\n`"
                + Main.prefix + "group remove groupNumber` - Removes the given group from the list (must be done by group creator).\n\n`"
                + Main.prefix + "group leave groupNumber` - Leaves a group (group will be automatically removed if 0 people remain).", "Group");
    }

    public void action(String[] args, MessageReceivedEvent event){
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }
        int num;

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) { //list
            if(groups.size() == 0){
                event.getChannel().sendMessage("No currently active groups!").queue();
            }else{
                for(int i = 0; i < groups.size(); ++i){
                    groups.get(i).printInfo(i+1, event);
                }
            }
        }else if(args.length == 2){ //join + remove
            if(args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("remove")){
                try{
                    num = Integer.parseInt(args[1])-1;
                }catch(NumberFormatException e){
                    event.getChannel().sendMessage("Invalid argument (`groupNumber` must be a number). Type `" + Main.prefix + "group help` for more info.").queue();
                    return;
                }

                if(num > groups.size()){
                    event.getChannel().sendMessage("There are only " + groups.size() + " active groups right now!").queue();
                }else if(args[0].equalsIgnoreCase("join")) {
                    groups.get(num).addPlayer(event.getMember().getEffectiveName(), event);
                }else if(args[0].equalsIgnoreCase("leave")){
                    groups.get(num).removePlayer(event.getMember().getEffectiveName(), event);
                    if(groups.get(num).isEmpty()){
                        groups.remove(num);
                    }
                }else if(args[0].equalsIgnoreCase("remove")){
                    if(event.getMember().getEffectiveName().equalsIgnoreCase(groups.get(num).getCreator())){
                        event.getChannel().sendMessage("Group removed!").queue();
                        groups.remove(num);
                    }else{
                        event.getChannel().sendMessage("Groups can only be removed by their creator!").queue();
                    }
                }
            }else{
                event.getChannel().sendMessage("Invalid format! Type `" + Main.prefix + "group help` for more info.").queue();
            }
        }else if(args.length >= 3 && args[0].equalsIgnoreCase("add")){
            try{
                num = Integer.parseInt(args[1]);
            }catch(NumberFormatException e){
                event.getChannel().sendMessage("Invalid argument (`size` must be a number). Type `" + Main.prefix + "group help` for more info.").queue();
                return;
            }

            String title = "";
            for(int i = 2; i < args.length; ++i){
                title = title.concat(args[i] + " ");
            }

            groups.add(new GameGroup(title, num, event.getMember().getEffectiveName(), event));
        }else{
            event.getChannel().sendMessage("Invalid format! Type `" + Main.prefix + "group help` for more info.").queue();
        }
    }
}