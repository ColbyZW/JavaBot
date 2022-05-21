package JavaBot.Database;

import org.bson.types.ObjectId;

public class User {
	private String username;
	private ObjectId id;

	public User(){}

	public User(String username) {
		this.username = username;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + "]";
	}

}
