package JavaBot.Listeners.Utils;

import JavaBot.Database.DiscordUser;
import net.dv8tion.jda.api.entities.MessageChannel;

abstract public class Command {
	private String command;
	private DiscordUser author;
	private String[] splitCommand;

	abstract public void runCommand(MessageChannel channel);

}
