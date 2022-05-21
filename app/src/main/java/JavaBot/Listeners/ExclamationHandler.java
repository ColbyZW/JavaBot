package JavaBot.Listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ExclamationHandler extends ListenerAdapter
{
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.getAuthor().isBot()) return;
		Message msg = event.getMessage();
		char[] characters = msg.getContentRaw().toCharArray();
		if(characters[0] == '!')
		{
			parseEvent(event);
		}
	}

	private void parseEvent(MessageReceivedEvent event)
	{
		//Grab the message from the event
		Message msg = event.getMessage();
		String content = msg.getContentRaw();
		//Get the author from the event
		User sender = event.getAuthor();
		CommandFactory factory = new CommandFactory(content, sender);
		//Based on the event passed in, return a Command from the factory
		Command cmd = factory.getCommand();
		//Run the command
		cmd.runCommand(event.getChannel());
	}
}
