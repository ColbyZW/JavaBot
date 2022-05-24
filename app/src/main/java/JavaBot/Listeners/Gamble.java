package JavaBot.Listeners;

import java.util.Random;

import org.bson.Document;

import JavaBot.Listeners.Utils.*;
import JavaBot.Database.DiscordUser;
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

	//Runs the !gamble command and checks the users money
	public void runCommand(MessageChannel channel)
	{
		if(!verifyCommand()) {
			channel.sendMessage("Please provide an appropriate amount: (!gamble 100)").queue();
			return;
		}
		MongoConnection db = new MongoConnection();
		if(!db.getStatus()) {
			channel.sendMessage("Unable to load data :(").queue();
			return;
		}
		DiscordUser doc = db.findUser(author.getName());	
		int money = Integer.parseInt(splitCommand[1]);
		if(money > doc.getMoney()) {
			channel.sendMessage("You dont have enough money for that.").queue();
			db.close();
			return;
		}
		if(gamble()) {
			db.addMoney(doc, money*2);
			channel.sendMessage("You just won " + money*2 + " moneys").queue();
		} else {
			db.addMoney(doc, -money);
			channel.sendMessage("You just lost " + money + " moneys").queue();
		}
		db.close();
	}

	//Rolls a random number and if the number is below 35, "true" is returned
	private Boolean gamble() {
		Random rand = new Random();
		int val = rand.nextInt() % 100;
		val = Math.abs(val);
		return val <= 35;
	}

	//Verifies that the correct arguments were passed in when calling gamble
	private Boolean verifyCommand() {
		//Verify that a second argument was passed in
		if(splitCommand.length <= 1) {
			return false;
		}
		//Verify that the argument is an integer
		try {
			Integer.parseInt(splitCommand[1]);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
