package bemsih.databaslaboration1.Model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
/**
 * Provides functionality to establish a connection to the database.
 */
public class DatabaseConnection {
    private static final String CONNECTION_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "Laboration1";

    public static MongoDatabase getConnection() {
        MongoClient client = MongoClients.create(CONNECTION_URI);
        return client.getDatabase(DATABASE_NAME);
    }
}
