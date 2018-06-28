package com.lunarbot.commands;

/*
    * LunarBot v1.4 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Command
    * Abstract class that is extended to all commands. Mainly used as a container for command information such as category, help, etc.
    * Secondary usage of this class is to provide useful static methods that all commands can access (formatting time, setting up embeds, etc.).
 */

import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Command {
    private Category category;
    private String info, help, name;

    protected Command(Category category, String info, String help, String name){
        this.category = category;
        this.info = info;
        this.help = help;
        this.name = name;
    }

    protected static String formatTime(OffsetDateTime offset, MessageReceivedEvent event){
        //Takes the time of the request and returns it in format M/dd/yyyy, h:mm:ss AM/PM UTC
        //Can take in an pre-determined OffsetDateTime (used in User) or take the time from the event.
        DateTimeFormatter format = DateTimeFormatter.ofPattern("M/dd/yyyy, h:mm:ss a 'UTC'");

        if(offset != null){
            return offset.format(format);
        }else{
            return event.getMessage().getCreationTime().format(format);
        }
    }

    protected static boolean isVoiceOk(GuildVoiceState botState, GuildVoiceState userState, MessageChannel channel){
        if(!botState.inVoiceChannel() || !userState.inVoiceChannel() || !botState.getChannel().getId().equalsIgnoreCase(userState.getChannel().getId())){
            channel.sendMessage(":x: We both must be in voice together - make sure we are connected to the same channel before calling a music command.").queue();
            return false;
        }
        return true;
    }

    protected boolean checkHelp(String[] args, MessageReceivedEvent event) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                embedHelp(event, getHelp(), getName());
                return true;
            }
        }
        return false;
    }

    private void embedHelp(MessageReceivedEvent event, String output, String commandName){
        EmbedBuilder embedBuilder = setupEmbed(event);
        embedBuilder.addField("Command Info: \"" + commandName + "\"", "`" + Main.prefix + output, true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public static EmbedBuilder setupEmbed(MessageReceivedEvent event){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor("LunarBot " + Main.version, null, null);
        embedBuilder.setColor(Color.decode("#99900CC"));
        embedBuilder.setFooter("Command received on: " + formatTime(null, event), event.getAuthor().getAvatarUrl());
        embedBuilder.setThumbnail(Main.THUMBNAIL);
        return embedBuilder;
    }

    public abstract void action(String[] args, MessageReceivedEvent event);

    public Category getCategory(){
        return category;
    }

    public String getInfo(){
        return info;
    }

    public String getName(){
        return name;
    }

    public String getHelp() {
        return help;
    }
}
