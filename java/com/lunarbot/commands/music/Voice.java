package com.lunarbot.commands.music;

/*
    * LunarBot v2.1.1 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Voice
    * Handles LunarBot joining or leaving voice channels.
    * Takes in format !voice join/leave
*/

import com.lunarbot.commands.Command;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.lunarbot.commands.Category.MUSIC;

public class Voice extends Command {
    public Voice(){
        super(MUSIC, "voice` - Controls LunarBot joining/leaving voice channels.", "voice join`: Summons LunarBot to a voice channel.\n`" + Main.prefix + "voice leave`: Disconnects LunarBot from a voice channel.", "Voice");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        if(args.length > 0){
            if(args[0].equalsIgnoreCase("join")){
                //Ensures LunarBot is connected to voice before continuing
                if(!event.getMember().getVoiceState().inVoiceChannel() || event.getGuild().getSelfMember().getVoiceState().inVoiceChannel()){
                    event.getChannel().sendMessage(":x: Either you're not in a voice channel, or I already am.").queue();
                }else{
                    event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
                    event.getChannel().sendMessage(":heavy_check_mark: Joining voice channel \"" + event.getMember().getVoiceState().getChannel().getName() + "\".").queue();
                }
            }else if(args[0].equalsIgnoreCase("leave")){
                if(isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
                    Main.scheduler.clearQueue(event);
                    Main.player.stopTrack();
                    event.getGuild().getAudioManager().closeAudioConnection();
                    event.getChannel().sendMessage(":wave: Left voice channel \"" + event.getGuild().getSelfMember().getVoiceState().getChannel().getName() + "\".").queue();
                }
            }else{
                event.getChannel().sendMessage("Invalid format! Type `" + Main.prefix + "voice help` for more info.").queue();
            }
        }else{
            event.getChannel().sendMessage("Invalid format! Type `" + Main.prefix + "voice help` for more info.").queue();
        }
    }
}