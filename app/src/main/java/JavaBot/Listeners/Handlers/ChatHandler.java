package JavaBot.Listeners.Handlers;

import JavaBot.Database.DiscordUser;
import JavaBot.Database.MongoConnection;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChatHandler extends ListenerAdapter {

	/**
	 * Function is called every time a chat message is sent in discord
	 * Goes and increments the users sent message counter in the database
	 * @param event The message event
	 */
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		MongoConnection db = new MongoConnection();
		//Finds the user in the database
		DiscordUser user = db.findUser(event.getAuthor().getName());
		//If the user doesn't exist add them to the database
		if(user == null) 
		{
			user = db.addUser(event.getAuthor().getName());
		}
		db.addMessage(user);
		//If the user has sent a multiple of 10 messages give them some money
		if(user.getMessagesSent() % 10 == 0) {
			db.addMoney(user, 100);
		}
		//Close the connection to the database
		db.close();
	}
	
}
