package JavaBot.Listeners;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;



import JavaBot.Listeners.Utils.Command;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Runescape extends Command {

	private String command;


	public Runescape(String command, User author) {
		String[] splitCommand = command.split(" ");
		splitCommand[0] = "";
		this.command = parseCommand(splitCommand);	
	}

	private String parseCommand(String[] parsed) {
		StringBuilder str = new StringBuilder();
		for(String fragment : parsed) {
			str.append(fragment);
			str.append("%20");
		}
		return str.toString();
	} 

	public void runCommand(MessageChannel channel) {
		channel.sendTyping().queue();
		String[] tokens = getResponse(command);
		String resultLink = getLink(tokens);
		if(resultLink == null) {
			channel.sendMessage("Unable to find that on the wiki").queue();
			return;
		}
		channel.sendMessage("Here is a link to the wiki: " + resultLink).queue();
	} 

	//Takes the tokens and finds the first link in the token list that leads to the wiki
	//Returns a completed link to the wiki
	private String getLink(String[] tokens) {
		if(tokens == null) {
			return null; 
		}
		String result = null;
		for(String tok : tokens) {
			if(tok.contains("https://oldschool.runescape.wiki")) {
				result = tok;	
				return result;
			}
		}
		return null; 
	}

	//Creates an HTTP request with the search term on the OSRS wiki
	//Parses the HTTP response into an array of strings and returns that
	//The Strings or tokens are split each time a [,],, are found
	private String[] getResponse(String searchTerm) {
		var client = HttpClient.newHttpClient();
		var request = java.net.http.HttpRequest.newBuilder(
				URI.create("https://oldschool.runescape.wiki/api.php?action=opensearch&format=json&search=" + searchTerm))
			.header("accept", "application/json")
			.build();
		HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
			String[] tokens = response.body().split("\\[|\\]|\"|,");
			return tokens;
		} catch(Exception e) {
			return null;
		}
		
	}
	
}

