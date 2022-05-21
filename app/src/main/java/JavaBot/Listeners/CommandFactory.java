package JavaBot.Listeners;

import net.dv8tion.jda.api.entities.User;

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
				Command cmd = new Gamble(command, author);
				return cmd;
			default:
				return new Default(command, author); 
		}
	}
}
