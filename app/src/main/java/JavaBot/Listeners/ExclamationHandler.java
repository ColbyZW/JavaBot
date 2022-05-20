package JavaBot.Listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ExclamationHandler extends ListenerAdapter
{
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.getAuthor().isBot()) return;
		Message msg = event.getMessage();
		String content = msg.getContentRaw();
		char[] characters = content.toCharArray();
		if(characters[0] == '!')
		{
			MessageChannel channel = event.getChannel();
			channel.sendMessage("Got your message!").queue();
		}
	}
}
