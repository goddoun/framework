

package kr.com.illootech.framework.databases.nosql.mongodb.example;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;
import org.bson.Document;
import com.mongodb.MongoClient;

public class MongoDBJDBC
{
    public static void main(final String[] args) {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient("localhost", 27017);
            final MongoDatabase db = mongoClient.getDatabase("BWLDATA");
            final MongoCollection<Document> collections = (MongoCollection<Document>)db.getCollection("BWLDATA");
            System.out.println("DB NAME :: " + db.getName());
            final FindIterable<Document> iterate = (FindIterable<Document>)collections.find();
            for (final Document document : iterate) {
                final String JsonResult = document.toJson();
                System.out.println(JsonResult);
            }
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
        catch (MongoException e) {
            e.printStackTrace();
        }
        finally {
            mongoClient = null;
        }
    }
}
