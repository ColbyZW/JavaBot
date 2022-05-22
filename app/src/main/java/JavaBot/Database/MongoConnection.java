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

	private MongoClient mongoClient;	
	private String uri;
	private CodecRegistry pojoCodecRegistry;

	//Opens up the initial mongo connection and stores it in an internal variable
	public MongoConnection() {
		Dotenv dotenv = Dotenv.load();
		this.uri = dotenv.get("URI");
		try {
			//Registers a codec registry to allow us to use custom classes with Mongo
			CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
			 pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
			this.mongoClient = MongoClients.create(uri);
		} catch (Exception e) {
			this.mongoClient = null;
		}
	}	

	//Returns the collection of just documents
	private MongoCollection<Document> getCollection() {
		MongoDatabase db = mongoClient.getDatabase("botDatabase");
		return db.getCollection("discord");
	}

	//Gets the collection of Users
	private MongoCollection<DiscordUser> getUserCollection() {
		MongoDatabase db = mongoClient.getDatabase("botDatabase").withCodecRegistry(pojoCodecRegistry);
		return db.getCollection("discord", DiscordUser.class);
	}

	//Finds a user in the database and returns their document
	public DiscordUser findUser(String username) {
		MongoCollection<DiscordUser> coll = getUserCollection(); 
		DiscordUser doc = coll.find(eq("username", username)).first();
		return doc;
	}

	//Adds a User to the database using the user POJO
	public DiscordUser addUser(String username) {
		//Creates a new user with the provided username
		DiscordUser user = new DiscordUser(username);
		//Grabs the database using the codec registry to allow us to use the custom User class
		MongoCollection<DiscordUser> collection = getUserCollection(); 
		collection.insertOne(user);
		//Return a reference to the user that was just added
		return findUser(username);
	}
	

	//Adds to the users message count
	public void addMessage(DiscordUser user) { 
		MongoCollection<DiscordUser> collection = getUserCollection();
		collection.updateOne(eq("username", user.getUsername()), Updates.inc("messagesSent", 1), new UpdateOptions().upsert(true));
	}
	
	//Adds money to the user in the database
	public void addMoney(DiscordUser user, int amount) {
		MongoCollection<DiscordUser> collection = getUserCollection();
		collection.updateOne(eq("username", user.getUsername()), Updates.inc("money", amount), new UpdateOptions().upsert(true));
	}

	//Closes the connection
	public void close() {
		mongoClient.close();
	}

}
