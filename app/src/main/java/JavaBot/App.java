/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package JavaBot;

import javax.security.auth.login.LoginException;

import JavaBot.Listeners.ExclamationHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class App {
	public static void main(String[] args) throws LoginException {
		JDA api = JDABuilder
			.createDefault("OTc3MDY1MzE3NzQ4NTk2Nzg3.G2HN1e.y0Uo4xiHWzUfgrpHI9sRtrh4EWgTjSd7KfkC3I")
			.addEventListeners(new MessageHandler(), new ExclamationHandler()) 
			.build();
	}
}