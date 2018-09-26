package com.lunarbot.core.bot;

/*
    * LunarBot v2.5 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Listener
    * Handles the process of processing commands and listening for events.
 */

import com.lunarbot.commands.toontown.Roles;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Listener extends ListenerAdapter {
    public void onGuildMemberJoin(GuildMemberJoinEvent event){
        event.getGuild().getTextChannelById("461074685678059521").sendMessage("Welcome to the Lunar Draconis Discord server, " + event.getMember().getAsMention() + "! Please read over "
                + "#rules, type `!agree` here in #welcome, then you will then be able to access the rest of the server.\n\nType `!role` in #bot-commands to choose a Toon-type role.").queue();
    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event){
        event.getGuild().getTextChannelById("456158433549352973").sendMessage(event.getMember().getEffectiveName() + " has left the server.").queue();
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
        if(event.getChannelLeft().getMembers().size() == 1){
            Main.scheduler.clearQueue(null);
            Main.player.stopTrack();
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }

    public void onGenericGuildMessageReaction(GenericGuildMessageReactionEvent event){
        Role role;
        if(event.getMessageId().equalsIgnoreCase(Roles.messageID) && !event.getReaction().isSelf() && event.getMember().getUser().getId().equalsIgnoreCase(Roles.senderID)){
            if(event.getReactionEmote().getName().equalsIgnoreCase("1⃣")){
                role = event.getGuild().getRolesByName("Zerker Type Toon", true).get(0);
            }else if(event.getReactionEmote().getName().equalsIgnoreCase("2⃣")){
                role = event.getGuild().getRolesByName("Support Type Toon", true).get(0);
            }else if(event.getReactionEmote().getName().equalsIgnoreCase("3⃣")){
                role = event.getGuild().getRolesByName("Hybrid Zerker Type Toon", true).get(0);
            }else if(event.getReactionEmote().getName().equalsIgnoreCase("4⃣")){
                role = event.getGuild().getRolesByName("Hybrid Support Type Toon", true).get(0);
            }else if(event.getReactionEmote().getName().equalsIgnoreCase("5⃣")){
                role = event.getGuild().getRolesByName("Jack Of All Trades Type Toon", true).get(0);
            }else{
                return;
            }

            if(event.getMember().getRoles().contains(role)){
                event.getGuild().getController().removeRolesFromMember(event.getMember(), role).queue();
                event.getChannel().sendMessage("Role \"" + role.getName() + "\" successfully removed.").queue();
            }else{
                event.getGuild().getController().addSingleRoleToMember(event.getMember(), role).queue();
                event.getChannel().sendMessage("Role \"" + role.getName() + "\" successfully added.").queue();
            }
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        //Checks that if the message received is a bot, ignore it and don't increment message count.
        if(event.getAuthor().isBot()){
            return;
        }

        if(event.getMessage().getContentDisplay().toLowerCase().contains("oldman")){
            event.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
            Emote satan = event.getGuild().getEmotesByName("Satan", true).get(0);
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you may only say " + satan.getAsMention() + "'s name once heck has frozen over.").queue();
            return;
        }

        if(!event.getChannel().getId().equalsIgnoreCase("456158433549352973")){
            String[] splitMessage = event.getMessage().getContentDisplay().split("\\s");
            for(String messageWord : splitMessage){
                for(String badWord : Main.badWords){
                    if(messageWord.equalsIgnoreCase(badWord)){
                        event.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
                        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " No bad language! Keep it PG.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                        return;
                    }
                }
            }
        }

        ++Main.messageCount;

        if(event.getMessage().getContentDisplay().equalsIgnoreCase(Main.prefix + "agree")){
            Role clubMember = event.getGuild().getRolesByName("Club Member", true).get(0);
            if(!event.getMember().getRoles().contains(clubMember)){
                event.getGuild().getController().addRolesToMember(event.getMember(), clubMember).queue();
            }
            return;
        }

        //LunarBot only processes commands if they start with ! or an @mention to its name
        if(event.getMessage().getContentDisplay().startsWith(Main.prefix)) {
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
        }else if(Arrays.stream(Main.allowedChannels).parallel().noneMatch(event.getChannel().getId()::contains)){
            event.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " No bot commands here! Only use bot commands in #bot-commands.").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
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
            event.getChannel().sendMessage("Invalid command name! Type `" + Main.prefix + "list help` for more info.").queue();
        }
    }
}
