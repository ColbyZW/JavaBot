package JavaBot.Listeners;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;


import JavaBot.Listeners.Utils.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class GrandExchange extends Command {

	private String command;
	private String search;

	public GrandExchange(String command, User user) {
		String[] splitCommand = command.split(" ");
		splitCommand[0] = "";
		this.search = parsedCommand(splitCommand);
		this.command = getSearchQuery(splitCommand);
	}

	//Gets the plain text search query for the embed
	private String getSearchQuery(String[] splitCommand) {
		StringBuilder str = new StringBuilder();
		for(String tok : splitCommand) {
			str.append(tok);
			str.append(" ");
		}
		return str.toString();
	}

	//Parses the searchQuery and turns it into one usable by the API
	private String parsedCommand(String[] splitCommand) {
		StringBuilder str = new StringBuilder();
		for(int i = 1; i < splitCommand.length; i++) {
			str.append(splitCommand[i]);
			if(i == splitCommand.length-1) break;
			str.append("%20");
		}
		return str.toString();
	}

	//Searches the GE API for an item
	public void runCommand(MessageChannel channel) {
		channel.sendTyping().queue();
		String[] tokens = getResponse(search);	
		String price = getPrice(tokens);
		if(price == null) {
			channel.sendMessage("Unable to find that item").queue();
			return;
		}
		EmbedBuilder embed = new EmbedBuilder();
		embed.addField(command, "Price: " + price + "gp" , false);
		channel.sendMessageEmbeds(embed.build()).queue();
	}

	//Takes in an array of tokens and finds the price
	//Goes through the price and adds a "," for every thousand
	//Returns the price
	private String getPrice(String[] tokens) {
		String price = null;
		for(int i = 0; i < tokens.length; i++) {
			if(tokens[i].equals("price")) {
				price = tokens[i+1];
			}
		}
		if(price == null) { return null; };
		String[] splitPrice = price.split("");
		StringBuilder str = new StringBuilder();
		for(int i = 1; i < splitPrice.length; i++) {
			if((splitPrice.length-1-i) % 3 == 0 && i != splitPrice.length - 1) {
				str.append(splitPrice[i]);
				str.append(",");
			} else {
				str.append(splitPrice[i]);
			}
		}	
		return str.toString();
	}

	//Creates an Http get request to the Grand Exchange api
	//Returns an array of tokens (the JSON result parsed and split into individual fields)
	private String[] getResponse(String searchTerm) {
		var client = HttpClient.newHttpClient();
		java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(
				URI.create("https://api.weirdgloop.org/exchange/history/osrs/latest?name="+ searchTerm))
			.header("accept", "application/json")
			.build();
		HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
			String[] tokens = response.body().split("\\{|\\}|\"|,");
			return tokens;
		} catch(Exception e) {
			return null;
		}
	}
}
