package JavaBot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageHandler extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.getAuthor().isBot()) return;
		Message msg = event.getMessage();
		String content = msg.getContentRaw();

		if(content.equals("!ping"))
		{
			MessageChannel channel = event.getChannel();
			channel.sendMessage("Pong!").queue();
		}
	}
		
}
