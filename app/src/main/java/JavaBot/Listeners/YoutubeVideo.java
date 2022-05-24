package JavaBot.Listeners;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import JavaBot.Listeners.Utils.Command;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class YoutubeVideo extends Command {
	
	private final String APP_NAME = "JAVA_BOT";
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private String key;
	private String command;

	private String[] splitCommand;
	

	public YoutubeVideo(String command, User author){

		Dotenv dotenv = Dotenv.load();
		this.key = dotenv.get("YOUTUBE_API");

		this.command = command;
		this.splitCommand = command.split(" ");
		
	}

	//Returns an API client service to use to make requests
	private YouTube getService() throws GeneralSecurityException, IOException {
		final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
			.setApplicationName(APP_NAME)
			.build();
	}

	//Searches Youtube API for a query and returns a completed link
	private String getVideo(String searchTerms) throws GeneralSecurityException, IOException, GoogleJsonResponseException{
		String[] terms = searchTerms.split(" ");
		terms[0] = "";
		String query = String.join("+", terms);
		YouTube youtubeService = getService();
		YouTube.Search.List req = youtubeService.search().list("snippet");
		SearchListResponse response = req.setKey(key)
			.setMaxResults(1L)
			.setQ(query)
			.execute();
		String videoId = response.getItems().get(0).getId().getVideoId();
		return "https://www.youtube.com/watch?v="+videoId;
		
	} 

	//Sends a youtube video in the discord channel if one was found
	public void runCommand(MessageChannel channel) {
		if(splitCommand.length <= 1) {
			channel.sendMessage("Please provide a video title after the command").queue();
			return;
		}	
		try {
			String result = getVideo(command);
			String response = "Here is your video! \n";
			channel.sendMessage(response + result).queue();
		} catch (Exception e){
			channel.sendMessage("Issue retrieving video :(").queue();
		}

	}


}
