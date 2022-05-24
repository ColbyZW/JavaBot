package JavaBot.Listeners;

import JavaBot.Listeners.Utils.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Help extends Command {


	public void runCommand(MessageChannel channel) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Commands");
		embed.addField("!help", "Display all of the commands available", false);
		embed.addField("!gamble <amount>", "Gambles some of your moneys", false);
		embed.addField("!youtube <search>", "Searches youtube for whatever you type in and returns the video", false);
		embed.addField("!profile", "Returns some of your server statistics like your money and how many messages you've sent", false);
		embed.setColor(Color.CYAN);
		channel.sendMessageEmbeds(embed.build()).queue();
	}

}
