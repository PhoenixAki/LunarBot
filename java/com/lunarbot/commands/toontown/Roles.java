package com.lunarbot.commands.toontown;

/*
  * LunarBot v2.4 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
  *
  * Roles
  * Manages roles for a user.
  * Takes in format !role
*/

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.TOONTOWN;

public class Roles extends Command {
    public static String messageID, senderID;

    public Roles() {
        super(TOONTOWN, "role` - Manages roles for a user.", "role` - Manages roles for a user. React to bot messages to make a decision.", "Group");
    }

    public void action(String[] args, MessageReceivedEvent event){
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        event.getChannel().sendMessage("So " + event.getMember().getAsMention() + ", you want to add a toon role?\n\n***Here is the list of roles we have available for toons:***\n" +
                ":one: **Zerker Type Toon** - Primarily a damage-dealing toon (e.g. using zap and trap).\n" +
                ":two: **Support Type Toon** - Primarily support-healing toon (e.g. using toon-up and squirt).\n" +
                ":three: **Hybrid Zerker Type Toon** - Primarily damage-dealing mixed with some support (e.g. using toon-up and zap.)\n" +
                ":four: **Hybrid Support Type Toon** - Primarily support-healing mixed with some damage (e.g. using toon-up/lure and throw/sound.)\n" +
                ":five: **Jack of All Trades Toon** - Useful in various scenarios (e.g. having most gag tracks but not many prestiged.)\n\n" +
                "Click the number below of the toon role you'd like to have added.\nTo remove a toon role, click the number below of a toon role that you already have, and it will be removed.").queue(message -> {
                    message.addReaction("\u0031\u20E3").queue();
                    message.addReaction("\u0032\u20E3").queue();
                    message.addReaction("\u0033\u20E3").queue();
                    message.addReaction("\u0034\u20E3").queue();
                    message.addReaction("\u0035\u20E3").queue();
                    messageID = message.getId();
                    senderID = event.getMember().getUser().getId();
        });
    }
}
