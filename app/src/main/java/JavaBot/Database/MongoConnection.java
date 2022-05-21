package JavaBot.Database;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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

	//Finds a user in the database and returns their document
	public Document findUser(String username) {
		MongoDatabase db = mongoClient.getDatabase("botDatabase");
		MongoCollection<Document> coll = db.getCollection("discord");
		Document doc = coll.find(eq("username", username)).first();
		return doc;
	}

	//Adds a User to the database using the user POJO
	public Document addUser(String username) {
		//Creates a new user with the provided username
		User user = new User(username);
		//Grabs the database using the codec registry to allow us to use the custom User class
		MongoDatabase db = mongoClient.getDatabase("botDatabase").withCodecRegistry(pojoCodecRegistry);
		MongoCollection<User> collection = db.getCollection("discord", User.class);
		collection.insertOne(user);
		//Return a reference to the user that was just added
		return findUser(username);
	}


	//Closes the connection
	public void close() {
		mongoClient.close();
	}

}
