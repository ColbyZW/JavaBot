package JavaBot.Listeners.Handlers;

import JavaBot.Database.DiscordUser;
import JavaBot.Database.MongoConnection;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChatHandler extends ListenerAdapter {
	
	//Checks if the user is already in the database, if not adds them
	//Adds 1 to their sent message count
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		MongoConnection db = new MongoConnection();
		DiscordUser user = db.findUser(event.getAuthor().getName());	
		if(user == null) 
		{
			user = db.addUser(event.getAuthor().getName());
		}
		db.addMessage(user);
		if(user.getMessagesSent() % 10 == 0) {
			db.addMoney(user, 100);
		}
		db.close();
	}
	
}
