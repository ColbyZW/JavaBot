/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package JavaBot;

import javax.security.auth.login.LoginException;


import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import net.dv8tion.jda.api.JDA;
import JavaBot.Listeners.Handlers.ChatHandler;
import JavaBot.Listeners.Handlers.ExclamationHandler;
import net.dv8tion.jda.api.JDABuilder;

public class App {

	public static void main(String[] args) throws LoginException {
		//Get the twitch API token and load it into the bot
		Dotenv dotenv = Dotenv.configure().load();
		String token = dotenv.get("TWITCH_TOKEN");
		JDA api = JDABuilder
			.createDefault(token)
			.addEventListeners(new ExclamationHandler(), new ChatHandler()) 
			.build();
	}

}
