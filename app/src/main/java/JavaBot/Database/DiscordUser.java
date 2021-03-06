package JavaBot.Database;

import org.bson.types.ObjectId;

public class DiscordUser {
	/** Stores the username */
	private String username;

	/** Stores the user ID */
	private ObjectId id;

	/** Stores the users curret amount of money */
	private int money;

	/** Stores the amount of messages the user has sent */
	private int messagesSent;

	public DiscordUser(){}

	/**
	 * Default constructor for creating a new user
	 * @param username username of the user
	 */
	public DiscordUser(String username) {
		this.username = username;
		this.money = 0;
		this.messagesSent = 0;
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getMoney() {
		return money;
	}

	public void setMessagesSent(int messagesSent) {
		this.messagesSent = messagesSent;
	}

	public int getMessagesSent() {
		return messagesSent;
	}

	@Override
	public String toString() {
		return "DiscordUser [id=" + id + ", username=" + username + ", money=" + money + ", messagesSent=" + messagesSent + "]";
	}

}
