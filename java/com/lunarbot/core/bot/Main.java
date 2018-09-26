package com.lunarbot.core.bot;

/*
	* LunarBot v2.5 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
	* 
	* Main
	* Mostly startup tasks and variable storage for use from commands.
*/

import com.lunarbot.commands.Category;
import com.lunarbot.commands.Command;
import com.lunarbot.commands.info.*;
import com.lunarbot.commands.info.Shutdown;
import com.lunarbot.commands.music.*;
import com.lunarbot.commands.toontown.Gags;
import com.lunarbot.commands.toontown.Group;
import com.lunarbot.commands.toontown.Fix;
import com.lunarbot.commands.toontown.Roles;
import com.lunarbot.core.audio.AudioPlayerSendHandler;
import com.lunarbot.core.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    //Bot Info Variables
    public static final String THUMBNAIL = "https://i.imgur.com/ZQgB8FX.png", version = "v2.5", prefix = "!";
    public static long commandCount = 0, messageCount = 0, startupTime;
    private static String botToken;
    static String[] badWords = {"ass", "asshole", "bastard", "bitch", "cunt", "damn", "dick", "fag", "fuck", "nigger", "penis", "pussy", "sex", "shit", "vagina"};
    static String[] allowedChannels = {"457392726170796043", "456158433549352973"};
    public static HashSet<String> categories = new HashSet<>();

    //Main Variables (used in startup + updating files)
	public static AudioPlayer player;
    public static AudioPlayerManager playerManager;
    public static AudioPlayerSendHandler sendHandler;
    public static TrackScheduler scheduler;
    private static PrintWriter logWriter = null;

    //Reference Variables (used in other classes)
	public static File log;
	public static HashMap<String, Command> commands = new HashMap<>();
	
	public static void main(String[] args){
		//Opens file input streams and startup api object.
	    openFiles();

		try{
			JDA jda = new JDABuilder(AccountType.BOT).addEventListener(new Listener()).setToken(botToken).buildBlocking();
			jda.setAutoReconnect(true);
			jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "LunarBot " + version + " | " + Main.prefix + "list help"));
			startupTime = System.currentTimeMillis() + 14400000;
		}catch(Exception e){
			e.printStackTrace();
		}

		//Startup tasks: initialize music variables, populate lists, add commands, and log startup time.
		startup(startupTime);
	}

	private static void openFiles(){
		//Open files for reading input.
		try{
			log = new File("Log.txt");
			logWriter = new PrintWriter(log);
	        botToken = new Scanner(new File("Token.txt")).useDelimiter("\\n").next().trim();
	    }catch(FileNotFoundException e){
	        System.out.println("Can't find file(s)!");
	    }
	}
	
	private static void startup(long UtcTimeInMillis) {
		//Initializes player manager.
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        player = playerManager.createPlayer();
        scheduler = new TrackScheduler(player);
		sendHandler = new AudioPlayerSendHandler(player);

        //Parses GuildObject JSON information, and fills command list.
		addCommands();

		//Logs the bot startup time.
		SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy, h:mm:ss a 'UTC'");
		String time = sdf.format(UtcTimeInMillis);
		Main.updateLog("Username", "Command Name", "Date/Time");
		Main.updateLog("LunarBot", "Startup", time);
	}
	
	private static void addCommands() {
        //Info
		commands.put("list", new List());
		commands.put("log", new Log());
		commands.put("server", new Server());
		commands.put("shutdown", new Shutdown());
		commands.put("status", new Status());
		commands.put("user", new Users());

		//Music List
        commands.put("pause", new Pause());
        commands.put("play", new Play());
        commands.put("queue", new Queue());
        commands.put("remove", new Remove());
        commands.put("seek", new Seek());
        commands.put("skip", new Skip());
        commands.put("song", new Song());
        commands.put("stop", new Stop());
        commands.put("voice", new Voice());
        commands.put("volume", new Volume());

        //Toontown list
		commands.put("fix", new Fix());
		commands.put("gags", new Gags());
		commands.put("group", new Group());
		commands.put("role", new Roles());

        //Adding category names to hashset for comparisons later
        for(Category cat : Category.values()){
            categories.add(cat.name());
        }
	}

	public static void updateLog(String username, String command, String time){
		logWriter.println(username + "\t" + command + "\t" + time);
		logWriter.flush();
	}
}