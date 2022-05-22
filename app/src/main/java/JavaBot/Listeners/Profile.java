package JavaBot.Listeners;

import JavaBot.Listeners.Utils.*;
import JavaBot.Database.DiscordUser;
import JavaBot.Database.MongoConnection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Profile extends Command {
	private DiscordUser user;

	public Profile(String command, User user){
		MongoConnection db = new MongoConnection();
		this.user = db.findUser(user.getName());
		db.close();
	}

	public void runCommand(MessageChannel channel) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(user.getUsername(), null);
		embed.addField("Messages Sent", String.format("%s", user.getMessagesSent()), false);
		embed.addField("Wallet", String.format("%s", user.getMoney()), false);
		channel.sendMessageEmbeds(embed.build()).queue();

	}
}
