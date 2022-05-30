package JavaBot.Listeners.Utils;

import net.dv8tion.jda.api.entities.User;
import JavaBot.Listeners.*;

public class CommandFactory
{
	private String command;
	private User author;

	public CommandFactory(String command, User author)
	{
		this.command = command;
		this.author = author;
	}

	public Command getCommand()
	{
		String cleanCommand = command.replace("!", "");
		String[] parsedCommand = cleanCommand.split(" ");
		switch(parsedCommand[0]) {
			case "gamble":
				 return new Gamble(command, author);
			case "profile":
				 return new Profile(command, author);
			case "youtube":
				 return new YoutubeVideo(command, author);
			case "help":
				 return new Help();
			case "wiki":
				 return new Runescape(command, author);
			default:
				return new Default(command, author); 
		}
	}
}
