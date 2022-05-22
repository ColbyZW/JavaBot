package JavaBot.Listeners;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import JavaBot.Listeners.Utils.*;

public class Default extends Command 
{

	private String command;
	private User author;

	public Default(String command, User author) 
	{
		this.command = command;
		this.author = author;
	}

	public void runCommand(MessageChannel channel)
	{
		channel.sendMessage("Sorry, unknown command").queue();
	}
}
