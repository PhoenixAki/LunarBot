package com.lunarbot.commands.info;

/*
    * LunarBot v2.0 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Server
    * Outputs relevant information about the server.
    * Takes in format !server
 */

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

import static com.lunarbot.commands.Category.INFO;

public class Server extends Command {
    public Server(){
        super(INFO, "server` - Outputs info about the current server.", "server`: Outputs info about the current server (name, creation date, etc.).", "Server");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        embedOutput(event);
    }

    private void embedOutput(MessageReceivedEvent event){
        EmbedBuilder embedBuilder = setupEmbed(event);

        embedBuilder.setTitle("Server Name: " + event.getGuild().getName());
        embedBuilder.addField("Server Owner", event.getGuild().getOwner().getEffectiveName(), true);
        embedBuilder.addField("Creation Date", formatTime(event.getGuild().getCreationTime(), event), true);
        embedBuilder.addField("User Count", Integer.toString(event.getGuild().getMembers().size()), true);
        embedBuilder.addField("Role Count", Integer.toString(event.getGuild().getRoles().size()-1), true);
        embedBuilder.setThumbnail(event.getGuild().getIconUrl());
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}