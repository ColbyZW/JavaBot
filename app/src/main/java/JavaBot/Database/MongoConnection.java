package JavaBot.Database;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

import io.github.cdimascio.dotenv.Dotenv;

public class MongoConnection {

	/** Stores the instance of the MongoDB Connection */
	private MongoClient mongoClient;
	/** Stores the URI for the MongoDB Connection */
	private String uri;
	/** Stores our POJO which allows us to map the document in Mongo to a Java Object */
	private CodecRegistry pojoCodecRegistry;
	/** Success flag (?) */
	private Boolean success;

	/**
	 * Opens a mongo connection and stores the instance internally
 	 */
	public MongoConnection() {
		Dotenv dotenv = Dotenv.load();
		this.uri = dotenv.get("MONGO_URI");
		try {
			//Registers a codec registry to allow us to use custom classes with Mongo
			CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
			 pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
			this.mongoClient = MongoClients.create(uri);
			this.success = true;
		} catch (Exception e) {
			this.mongoClient = null;
			this.success = false;
		}
	}

	/**
	 * Queries the database and returns all the document in the discord index
	 * @return Collection of documents in the discord index
	 */
	private MongoCollection<Document> getCollection() {
		MongoDatabase db = mongoClient.getDatabase("botDatabase");
		return db.getCollection("discord");
	}

	/**
	 * Queries the database and returns a collection of the user documents
 	 * @return Collection of user documents from the database
	 */
	private MongoCollection<DiscordUser> getUserCollection() {
		MongoDatabase db = mongoClient.getDatabase("botDatabase").withCodecRegistry(pojoCodecRegistry);
		return db.getCollection("discord", DiscordUser.class);
	}

	/**
	 * Function to query the status of the database connection
 	 * @return whether the connection was successful or not
	 */
	public Boolean getStatus() {
		return success;
	}

	/**
	 * Searches the database for a specific user
	 * @param username discord username
	 * @return Document containing the user info
	 */
	public DiscordUser findUser(String username) {
		MongoCollection<DiscordUser> coll = getUserCollection(); 
		DiscordUser doc = coll.find(eq("username", username)).first();
		return doc;
	}

	/**
	 * Adds a user to the MongoDB
	 * @param username discord username
	 * @return A document containing the new users information
	 */
	public DiscordUser addUser(String username) {
		//Creates a new user with the provided username
		DiscordUser user = new DiscordUser(username);
		//Grabs the database using the codec registry to allow us to use the custom User class
		MongoCollection<DiscordUser> collection = getUserCollection(); 
		collection.insertOne(user);
		//Return a reference to the user that was just added
		return findUser(username);
	}

	/**
	 * Increments a users sent message counter
	 * @param user A reference to the discord user
	 */
	public void addMessage(DiscordUser user) { 
		MongoCollection<DiscordUser> collection = getUserCollection();
		collection.updateOne(eq("username", user.getUsername()), Updates.inc("messagesSent", 1), new UpdateOptions().upsert(true));
	}

	/**
	 * Increments the users total cash in the database
	 * @param user A reference to the discord user
	 * @param amount Amount of money to give the user
	 */
	public void addMoney(DiscordUser user, int amount) {
		MongoCollection<DiscordUser> collection = getUserCollection();
		collection.updateOne(eq("username", user.getUsername()), Updates.inc("money", amount), new UpdateOptions().upsert(true));
	}

	/**
	 * Close the MongoDB connection
	 */
	public void close() {
		mongoClient.close();
	}

}
