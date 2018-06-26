package com.lunarbot.commands.info;

/*
    * LunarBot v1.2 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Users
    * Returns relevant information about the mentioned user(s). Class name is Users to avoid naming conflict with JDA class User.
    * Takes in format !user @userHere
 */

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.INFO;

public class Users extends Command {
    public Users(){
        super(INFO, "user` - Outputs info about the mentioned user(s).", "user @userHere`: Outputs info about the mentioned user(s).", "User");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        //Reads in all the mentioned users one at a time and embed outputs each one
        if(event.getMessage().getMentionedUsers().size() > 0){
            for(User user : event.getMessage().getMentionedUsers()){
                embedOutput(event, event.getGuild().getMember(user), user);
            }
        }else{
            event.getChannel().sendMessage("Invalid format! Type `" + Main.prefix + "user help` for more info.").queue();
        }
    }

    private void embedOutput(MessageReceivedEvent event, Member member, User user){
        EmbedBuilder embedBuilder = setupEmbed(event);

        embedBuilder.setTitle("User: " + user.getName());
        embedBuilder.addField("Nickname", member.getEffectiveName(), false);
        embedBuilder.addField("Account Creation Date", formatTime(user.getCreationTime(), event), false);
        embedBuilder.addField("Server Join Date", formatTime(member.getJoinDate(), event), false);
        embedBuilder.setThumbnail(user.getAvatarUrl());
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
