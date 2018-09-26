package com.lunarbot.commands;

/*
    * LunarBot v2.5 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Voice
    * Handles LunarBot joining or leaving voice channels.
    * Takes in format !voice join/leave
*/

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class GameGroup {
    private String title;
    private ArrayList<String> players = new ArrayList<>();
    private int finalSize, currentSize;

    public GameGroup(String title, int finalSize, String creator, MessageReceivedEvent event){
        this.title = title;
        this.players.add(creator);
        currentSize = 1;
        this.finalSize = finalSize;
        event.getChannel().sendMessage("New group created with title \"" + title + "\" and size " + finalSize + ".").queue();
    }

    public String getCreator(){
        return players.get(0);
    }

    public boolean isEmpty(){
        return players.size() == 0;
    }

    public void addPlayer(String name, MessageReceivedEvent event){
        if(players.contains(name)){
            event.getChannel().sendMessage("You are already in this group!").queue();
        }else if(currentSize == finalSize){
            event.getChannel().sendMessage("Group \"" + title + "\" is full!").queue();
        }else{
            currentSize++;
            players.add(name);
            if(currentSize == finalSize){
                event.getChannel().sendMessage("Group \"" + title + "\" is now full!").queue();
            }else{
                event.getChannel().sendMessage(name + " has joined \"" + title + "\"!").queue();
            }
        }
    }

    public void removePlayer(String name, MessageReceivedEvent event){
        if(players.contains(name)){
            players.remove(name);
            event.getChannel().sendMessage(name + " has left " + title + ".").queue();
            currentSize--;
        }
    }

    private String listPlayers(){
        String list = "";
        for(int i = 0; i < players.size(); ++i){
            list = list.concat((i == players.size() - 1) ? players.get(i) : players.get(i) + ", ");
        }

        return list;
    }

    public void printInfo(int position, MessageReceivedEvent event){
        EmbedBuilder embedBuilder = Command.setupEmbed(event);

        embedBuilder.addField("Group " + position + ":", title, false);
        embedBuilder.addField("Current Player Count:", currentSize + "/" + finalSize, false);
        embedBuilder.addField("Players Joined:", listPlayers(), false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
