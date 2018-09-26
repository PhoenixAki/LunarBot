package com.lunarbot.commands.toontown;

/*
    * LunarBot v2.5 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
    *
    * Main
    * Mostly startup tasks and variable storage for use from commands.
*/

import com.lunarbot.commands.Command;
import com.lunarbot.commands.Gag;
import com.lunarbot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.lunarbot.commands.Category.TOONTOWN;

public class Gags extends Command {

    private ArrayList<String> tracks = new ArrayList<>(Arrays.asList("toonup", "trap", "lure", "sound", "squirt", "zap", "throw", "drop"));
    private ArrayList<String> gags = new ArrayList<>(Arrays.asList("Feather", "Megaphone", "Lipstick", "Bamboo Cane", "Pixie Dust", "Juggling Balls", "Treasure Chest", "High Dive",
            "Banana Peel", "Rake", "Spring Board", "Marbles", "Quicksand", "Trap Door", "Wrecking Ball", "TNT",
            "$1 Bill", "Small Magnet", "$5 Bill", "Big Magnet", "$10 Bill", "Hypno Goggles", "$50 Bill", "Presentation",
            "Bike Horn", "Whistle", "Kazoo", "Bugle", "Aoogah", "Elephant Trunk", "Foghorn", "Opera Singer",
            "Squirting Flower", "Glass of Water", "Squirt Gun", "Water Balloon", "Seltzer Bottle", "Fire Hose", "Storm Cloud", "Geyser",
            "Joy Buzzer", "Carpet", "Balloon", "Kart Battery", "Taser", "Broken T.V.", "Tesla Coil", "Lightning",
            "Cupcake", "Fruit Pie Slice", "Cream Pie Slice", "Birthday Cake Slice", "Whole Fruit Pie", "Whole Cream Pie", "Birthday Cake", "Wedding Cake",
            "Flower Pot", "Sandbag", "Bowling Ball", "Anvil", "Big Weight", "Safe", "Boulder", "Piano"));

    public Gags() {
        super(TOONTOWN, "gags` - Returns a randomized distribution of gags given a list of gag tracks.", "gags gagCarryMax trackOne trackTwo ...` - Given a gag carrying maximum and a list of gag tracks, returns a list of randomly distributed gags.", "Gags");
    }

    public void action(String[] args, MessageReceivedEvent event){
        Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
        if(checkHelp(args, event)) { return; }

        Random rng = new Random();
        int gagCarryMax, gagsRandomized = 0;
        ArrayList<String> tracksEntered = new ArrayList<>();
        ArrayList<Gag> gagList = new ArrayList<>();

        try{
            gagCarryMax = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            event.getChannel().sendMessage("Invalid gag carrying maximum entered! Command format is `" + Main.prefix + "gags gagCarryMax trackOne trackTwo ...`").queue();
            return;
        }

        //populating track list
        for(int i = 1; i < args.length; ++i){
            if(!tracks.contains(args[i])){
                event.getChannel().sendMessage("Invalid track name entered! Valid tracks are `toonup, trap, lure, sound, squirt, zap, throw, drop`.").queue();
                return;
            }else if(!tracksEntered.contains(args[i].toLowerCase())){
                tracksEntered.add(StringUtils.capitalize(args[i]));
            }
        }

        //populating gag list
        for(String track : tracksEntered){
            int startPoint = 0, limit = 0;

            switch(track){
                case "Toonup":
                    startPoint = 0;
                    break;
                case "Trap":
                    startPoint = 8;
                    break;
                case "Lure":
                    startPoint = 16;
                    break;
                case "Sound":
                    startPoint = 24;
                    break;
                case "Squirt":
                    startPoint = 32;
                    break;
                case "Zap":
                    startPoint = 40;
                    break;
                case "Throw":
                    startPoint = 48;
                    break;
                case "Drop":
                    startPoint = 56;
                    break;
            }

            do{
                switch(startPoint % 8){
                    case 0:
                        limit = 30;
                        break;
                    case 1:
                        limit = 25;
                        break;
                    case 2:
                        limit = 20;
                        break;
                    case 3:
                        limit = 15;
                        break;
                    case 4:
                        limit = 7;
                        break;
                    case 5:
                        limit = 3;
                        break;
                    case 6:
                        limit = 2;
                        break;
                    case 7:
                        limit = 1;
                        break;
                }

                gagList.add(new Gag(gags.get(startPoint), track, 0, limit));
                ++startPoint;
            }while(startPoint % 8 != 0);
        }

        //adding gags randomly
        while(gagsRandomized < gagCarryMax){
            int num = rng.nextInt(gagList.size());
            if(gagList.get(num).count +1 <= gagList.get(num).limit){
                gagList.get(num).count++;
                ++gagsRandomized;
            }
        }

        //output
        String output = "", gagAddition = "";
        int gagCounter = 0;

        for(String track : tracksEntered){
            output = output.concat("\n\n" + track.toUpperCase() + "\n");

            do{
                gagAddition = gagAddition.concat(gagList.get(gagCounter).name + ": " + gagList.get(gagCounter).count + "/" + gagList.get(gagCounter).limit + ", ");
                gagCounter++;
            }while(gagCounter % 8 != 0);

            output = output.concat(gagAddition.substring(0, gagAddition.length()-2));
            gagAddition = "";
        }

        event.getChannel().sendMessage("```java\n" + output + "```").queue();
    }
}
