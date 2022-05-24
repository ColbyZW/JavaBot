package JavaBot.Listeners;

import JavaBot.Listeners.Utils.*;
import JavaBot.Database.DiscordUser;
import JavaBot.Database.MongoConnection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import java.awt.Color;

public class Profile extends Command {
	private DiscordUser user;

	//Constructor function gets the user from the database and stores that info
	public Profile(String command, User user){
		MongoConnection db = new MongoConnection();
		this.user = db.findUser(user.getName());
		db.close();
	}

	//Creates an embed that displays users statistics in the server and sends it
	public void runCommand(MessageChannel channel) {
		if(user == null) {
			channel.sendMessage("Unable to find your profile :(").queue();
		}
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(user.getUsername(), null);
		embed.addField("Messages Sent", String.format("%s", user.getMessagesSent()), false);
		embed.addField("Wallet", String.format("%s", user.getMoney()), false);
		embed.setColor(Color.CYAN);
		channel.sendMessageEmbeds(embed.build()).queue();

	}
}
