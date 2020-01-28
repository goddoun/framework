

package kr.com.illootech.framework.databases.nosql.mongodb.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;
import java.util.List;
import com.mongodb.MongoClient;
import org.bson.Document;
import java.util.ArrayList;

public class MongoInsertEx
{
    public static void main(final String[] args) {
        try {
            final List<Document> seedData = new ArrayList<Document>();
            seedData.add(new Document("decade", (Object)"1970s").append("artist", (Object)"Debby Boone").append("song", (Object)"You Light Up My Life").append("weeksAtOne", (Object)10));
            seedData.add(new Document("decade", (Object)"1980s").append("artist", (Object)"Olivia Newton-John").append("song", (Object)"Physical").append("weeksAtOne", (Object)10));
            seedData.add(new Document("decade", (Object)"1990s").append("artist", (Object)"Mariah Carey").append("song", (Object)"One Sweet Day").append("weeksAtOne", (Object)16));
            final MongoClient client = new MongoClient("localhost", 1223);
            final MongoDatabase db = client.getDatabase("bluewalnut");
            final MongoCollection<Document> songs = (MongoCollection<Document>)db.getCollection("songs");
            songs.insertMany((List)seedData);
            final Document tmpDoc = new Document();
            tmpDoc.append("decade", (Object)"1990s");
            tmpDoc.append("artist", (Object)"Mariah Carey");
            tmpDoc.append("song", (Object)"One Sweet Day");
            songs.insertOne((Object)tmpDoc);
            if (client != null) {
                client.close();
            }
        }
        catch (MongoException e) {
            e.printStackTrace();
        }
    }
}
