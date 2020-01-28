

package kr.com.illootech.framework.databases.nosql.mongodb.example;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;
import com.mongodb.MongoClient;

public class MongoEx
{
    public static void main(final String[] args) {
        MongoClient mongo = null;
        MongoDatabase db = null;
        MongoCollection<Document> collection = null;
        try {
            mongo = new MongoClient("localhost", 27017);
            db = mongo.getDatabase("bluewalnut");
            collection = (MongoCollection<Document>)db.getCollection("BWLDATA");
            final FindIterable<Document> it = (FindIterable<Document>)collection.find();
            final MongoCursor<Document> cursor = (MongoCursor<Document>)it.iterator();
            System.out.println(">>>>find<<<");
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
            if (mongo != null) {
                mongo.close();
            }
        }
        catch (MongoException e) {
            e.printStackTrace();
        }
    }
}
