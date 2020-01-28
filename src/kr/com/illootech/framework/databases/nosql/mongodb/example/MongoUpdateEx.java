

package kr.com.illootech.framework.databases.nosql.mongodb.example;

import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

public class MongoUpdateEx
{
    public static void printAllDocuments(final MongoCollection<Document> collection) {
        final FindIterable<Document> it = (FindIterable<Document>)collection.find();
        final MongoCursor<Document> cursor = (MongoCursor<Document>)it.iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }
    
    public static void removeAllDocuments(final MongoCollection<Document> collection) {
        collection.deleteOne((Bson)new Document());
    }
    
    public static void insertDummyDocuments(final MongoCollection<Document> collection) {
        final Document document = new Document();
        document.put("hosting", (Object)"hostA");
        document.put("type", (Object)"vps");
        document.put("clients", (Object)1000);
        final Document document2 = new Document();
        document2.put("hosting", (Object)"hostB");
        document2.put("type", (Object)"dedicated server");
        document2.put("clients", (Object)100);
        final Document document3 = new Document();
        document3.put("hosting", (Object)"hostC");
        document3.put("type", (Object)"vps");
        document3.put("clients", (Object)900);
        collection.insertOne((Object)document);
        collection.insertOne((Object)document2);
        collection.insertOne((Object)document3);
    }
    
    public static void main(final String[] args) {
        MongoClient mongo = null;
        MongoDatabase db = null;
        MongoCollection<Document> collection = null;
        try {
            mongo = new MongoClient("localhost", 27017);
            db = mongo.getDatabase("bluewalnut");
            collection = (MongoCollection<Document>)db.getCollection("BWLDATA");
            insertDummyDocuments(collection);
            final Document newDocument = new Document();
            newDocument.put("clients", (Object)110);
            final Document searchQuery = new Document().append("hosting", (Object)"hostB");
            collection.updateOne((Bson)searchQuery, (Bson)newDocument);
            printAllDocuments(collection);
            insertDummyDocuments(collection);
            final Document updateDocument = new Document();
            updateDocument.append("$set", (Object)new Document().append("clients", (Object)110));
            final Document searchQuery2 = new Document().append("hosting", (Object)"hostB");
            collection.updateOne((Bson)searchQuery2, (Bson)updateDocument);
            printAllDocuments(collection);
            removeAllDocuments(collection);
            insertDummyDocuments(collection);
            final Document newDocument2 = new Document().append("$inc", (Object)new BasicDBObject().append("clients", (Object)99));
            collection.updateOne((Bson)new BasicDBObject().append("hosting", (Object)"hostB"), (Bson)newDocument2);
            printAllDocuments(collection);
            insertDummyDocuments(collection);
            final Document updateQuery = new Document();
            updateQuery.append("$set", (Object)new Document().append("clients", (Object)"888"));
            final Document searchQuery3 = new Document();
            searchQuery3.append("type", (Object)"vps");
            collection.updateMany((Bson)searchQuery3, (Bson)updateQuery);
            printAllDocuments(collection);
            if (mongo != null) {
                mongo.close();
            }
        }
        catch (MongoException e) {
            e.printStackTrace();
        }
    }
}
