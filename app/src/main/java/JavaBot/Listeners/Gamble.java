package JavaBot.Listeners;

import org.bson.Document;

import JavaBot.Database.MongoConnection;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Gamble extends Command {
	
	private String command;
	private String[] splitCommand;
	private User author;

	public Gamble(String command, User author)
	{
		this.command = command;
		this.splitCommand = command.split(" ");
		this.author = author;

	}
	public void runCommand(MessageChannel channel)
	{
		MongoConnection db = new MongoConnection();
		Document doc = db.findUser(author.getName());	
		if(doc == null) {
			doc = db.addUser(author.getName());
		}
	}
}
