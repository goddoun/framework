

package kr.com.illootech.framework.databases.nosql.mongodb.example;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;
import org.bson.conversions.Bson;
import org.bson.Document;
import com.mongodb.MongoClient;

public class MongoDeleteEx
{
    public static void main(final String[] args) {
        MongoClient mongo = null;
        MongoDatabase db = null;
        MongoCollection<Document> collection = null;
        try {
            mongo = new MongoClient("localhost", 27017);
            db = mongo.getDatabase("bluewalnut");
            collection = (MongoCollection<Document>)db.getCollection("BWLDATA");
            final Document doc = new Document();
            doc.put("goodscnt", (Object)"44");
            collection.deleteOne((Bson)doc);
            final FindIterable<Document> findIt = (FindIterable<Document>)collection.find();
            final MongoCursor<Document> cursor = (MongoCursor<Document>)findIt.iterator();
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
        finally {
            collection = null;
            db = null;
            mongo = null;
        }
    }
}
