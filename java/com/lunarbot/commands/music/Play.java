package com.lunarbot.commands.music;

/*
	* LunarBot v2.2 by PhoenixAki: General purpose bot for usage in the TTCC Lunar Draconis clan server.
	*
	* Play
	* Queues a song or playlist to be played by the guild's AudioPlayer. Also resumes the player if paused/stopped.
	* Allows links from YouTube, SoundCloud, BandCamp, Vimeo, and live Twitch streams.
	* Takes in format !play link/searchTerms
 */

import com.lunarbot.commands.Command;
import com.lunarbot.core.audio.TrackInfo;
import com.lunarbot.core.bot.Main;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lunarbot.commands.Category.MUSIC;

public class Play extends Command {
	public Play() {
		super(MUSIC, "play` - Queues a song for playback. Unpauses the player if paused.", "play`: Resumes playback if previously paused or stopped.\n`" + Main.prefix + "play link/searchTerms`: Queues a song if directly linked, or searches youtube and queues the first result.", "Play");
	}

	public void action(String[] args, MessageReceivedEvent event) {
		Main.updateLog(event.getAuthor().getName(), getName(), formatTime(null, event));
		if(checkHelp(args, event)) { return; }
		final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://([A-Za-z0-9-._~/?#\\[\\]:!$&'()*+,;=]+)$");
		String input = "";

		//Ensures LunarBot is connected to voice before continuing
		if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
			return;
		}

		//Ensures the sending handler is properly set before continuing
		event.getGuild().getAudioManager().setSendingHandler(Main.sendHandler);

		if(args.length == 0){
			if(Main.player.isPaused()){
				Main.player.setPaused(false);
				event.getChannel().sendMessage("Song playback resumed.").queue();
			}else if(Main.player.getPlayingTrack() == null && Main.scheduler.hasNextTrack()){
				Main.scheduler.nextTrack();
				event.getChannel().sendMessage("Queue playback resumed.").queue();
			}else{
				event.getChannel().sendMessage("Either there isn't a paused/stopped song, or no songs are queued for playback.").queue();
			}
		}else{
			//If args[0] is a URL, process as-is. If not, prep for a youtube search.
			Matcher m = URL_PATTERN.matcher(args[0]);
			if(m.find()){
				input = args[0];
			}else{
				for(String arg : args){
					input = input.concat(arg);
				}
				input = "ytsearch:" + input;
				event.getChannel().sendMessage("Searching youtube...").queue();
			}

			Main.playerManager.loadItemOrdered(Main.playerManager, input, new AudioLoadResultHandler() {
				public void trackLoaded(AudioTrack track) {
					track.setUserData(new TrackInfo(event.getMember().getEffectiveName(), event.getChannel()));
					Main.scheduler.addTrack(track);

					if(Main.scheduler.queue.size() == 0){
						event.getChannel().sendMessage(":musical_note: Now Playing: **" + track.getInfo().title + "**, requested by **" + event.getMember().getEffectiveName() + "**").queue();
					}else{
						event.getChannel().sendMessage("Queued: **" + track.getInfo().title + "**").queue();
					}
				}

				public void playlistLoaded(AudioPlaylist playlist) {
					//If done via ytsearch, returns a playlist of results, but LunarBot loads just the first result
					if(playlist.isSearchResult()){
						AudioTrack track = playlist.getTracks().get(0);
						track.setUserData(new TrackInfo(event.getMember().getEffectiveName(), event.getChannel()));
						Main.scheduler.addTrack(track);

						if(Main.scheduler.queue.size() == 0){
							event.getChannel().sendMessage(":musical_note: Now Playing: **" + track.getInfo().title + "**, requested by **" + event.getMember().getEffectiveName() + "**").queue();
						}else{
							event.getChannel().sendMessage("Queued: **" + track.getInfo().title + "**").queue();
						}
					}else{
						Main.scheduler.addPlaylist(playlist, event);
						event.getChannel().sendMessage("Playlist loaded successfully!").queue();
					}
				}

				public void noMatches() {
					event.getChannel().sendMessage(":x: No match found.").queue();
				}

				public void loadFailed(FriendlyException e) {
					event.getChannel().sendMessage("Unknown error - please notify @Phoenix#0353.").queue();
					e.printStackTrace();
				}
			});
		}
	}
}