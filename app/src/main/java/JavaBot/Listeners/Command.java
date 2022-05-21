package JavaBot.Listeners;

import net.dv8tion.jda.api.entities.MessageChannel;

abstract public class Command {

	abstract void runCommand(MessageChannel channel);

}
