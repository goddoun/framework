

package kr.com.illootech.framework.databases.nosql.mongodb;

import com.mongodb.client.FindIterable;
import org.bson.conversions.Bson;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.mongodb.client.MongoDatabase;
import java.util.Iterator;
import java.util.Set;
import com.mongodb.MongoException;
import kr.com.illootech.framework.file.log.LoggerElements;
import java.util.LinkedHashMap;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import kr.com.illootech.framework.file.log.Logger;
import java.util.LinkedList;
import com.mongodb.DB;

public class MongoSQLHandler
{
    private final String procname = "MONGO.SQLHDL";
    
    public void aggregateData(final DB db, Object result, final String jscriptCode, final LinkedList<Object> args) {
        try {
            if (db == null) {
                return;
            }
            result = db.eval(jscriptCode, new Object[] { args });
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public void insertData(final DB db, final String tableName, final String[] insertColumn, final String[] insertValue) {
        DBCollection table = null;
        BasicDBObject query = null;
        try {
            if (db == null) {
                return;
            }
            table = db.getCollection(tableName);
            query = new BasicDBObject();
            for (int colCnt = insertColumn.length, i = 0; i < colCnt; ++i) {
                query.put((Object)insertColumn[i], (Object)insertValue[i]);
            }
            table.insert(new DBObject[] { (DBObject)query });
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public boolean insertData(final MongoCollection<Document> collection, final LinkedHashMap<String, String> dataList) {
        boolean result = false;
        try {
            if (collection == null || dataList == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Insert data-one to mongo. Collection or data is not available] - FAIL", "MONGO.SQLHDL", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            final Set<String> key = dataList.keySet();
            if (key == null || key.size() <= 0) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Insert data-one to mongo. Key is not available in this hashmap] - FAIL", "MONGO.SQLHDL", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            final Iterator<String> itKey = key.iterator();
            if (itKey == null || !itKey.hasNext()) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Insert data-one to mongo. Get key from Set<Key-String>] - FAIL", "MONGO.SQLHDL", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            final Document tmpDoc = new Document();
            while (itKey.hasNext()) {
                final String tmpKey = itKey.next();
                if (tmpKey == null) {
                    continue;
                }
                String tmpValue = dataList.get(tmpKey);
                if (tmpValue == null) {
                    tmpValue = "";
                }
                tmpDoc.append(tmpKey, (Object)tmpValue);
            }
            collection.insertOne((Object)tmpDoc);
            result = true;
            return result;
        }
        catch (MongoException e) {
            Logger.error((Exception)e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Insert data-one to mongo. Data '%3$s'] - %4$s", "MONGO.SQLHDL", result ? "RUNNING" : "ERROR", result ? dataList : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    public boolean insertDataList(final MongoDatabase db, final String collectionName, final LinkedList<HashMap<String, String>> dataList) {
        boolean result = false;
        MongoCollection<Document> collection = null;
        try {
            if (db == null) {
                return result;
            }
            collection = (MongoCollection<Document>)db.getCollection(collectionName);
            if (collection == null) {
                return result;
            }
            result = this.insertDataList(collection, dataList);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean insertDataList(final MongoCollection<Document> collection, final LinkedList<HashMap<String, String>> dataList) {
        boolean result = false;
        try {
            if (collection == null || dataList == null) {
                return result;
            }
            final List<Document> seedData = new ArrayList<Document>();
            for (int listSize = dataList.size(), i = 0; i < listSize; ++i) {
                final HashMap<String, String> tmpMap = dataList.get(i);
                if (tmpMap != null) {
                    final Set<String> key = tmpMap.keySet();
                    if (key == null || key.size() <= 0) {
                        return result;
                    }
                    final Iterator<String> itKey = key.iterator();
                    if (itKey == null || !itKey.hasNext()) {
                        return result;
                    }
                    final Document tmpDoc = new Document();
                    while (itKey.hasNext()) {
                        final String tmpKey = itKey.next();
                        if (tmpKey == null) {
                            continue;
                        }
                        final String tmpValue = tmpMap.get(tmpKey);
                        if (tmpValue == null) {
                            continue;
                        }
                        tmpDoc.append(tmpKey, (Object)tmpValue);
                    }
                    seedData.add(tmpDoc);
                }
            }
            collection.insertMany((List)seedData);
            result = true;
            return result;
        }
        catch (MongoException e) {
            Logger.error((Exception)e);
            return false;
        }
    }
    
    public void updateData(final DB db, final String collectionName, final String[] whereColumn, final String[] whereValue, final String[] updateColumn, final String[] updateValue) {
        DBCollection collection = null;
        BasicDBObject whereQuery = null;
        BasicDBObject updateQuery = null;
        try {
            collection = db.getCollection(collectionName);
            whereQuery = new BasicDBObject();
            updateQuery = new BasicDBObject();
            for (int whereColCnt = whereColumn.length, i = 0; i < whereColCnt; ++i) {
                whereQuery.put((Object)whereColumn[i], (Object)whereValue[i]);
            }
            for (int updateColCnt = updateColumn.length, j = 0; j < updateColCnt; ++j) {
                updateQuery.put((Object)updateColumn[j], (Object)updateValue[j]);
            }
            collection.update((DBObject)whereQuery, (DBObject)updateQuery);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public void updateData(final MongoDatabase db, final String collectionName, final String[] whereColumn, final String[] whereValue, final String[] updateColumn, final String[] updateValue) {
        MongoCollection<Document> collection = null;
        try {
            collection = (MongoCollection<Document>)db.getCollection(collectionName);
            final Document whereQuery = new Document();
            final Document updateQuery = new Document();
            for (int whereColCnt = whereColumn.length, i = 0; i < whereColCnt; ++i) {
                whereQuery.put(whereColumn[i], (Object)whereValue[i]);
            }
            for (int updateColCnt = updateColumn.length, j = 0; j < updateColCnt; ++j) {
                updateQuery.put(updateColumn[j], (Object)updateValue[j]);
            }
            collection.updateOne((Bson)whereQuery, (Bson)updateQuery);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public void deleteData(final DB db, final String tableName, final String[] whereColumn, final String[] whereValue) {
        DBCollection collection = null;
        BasicDBObject whereQuery = null;
        try {
            collection = db.getCollection(tableName);
            whereQuery = new BasicDBObject();
            for (int whereColCnt = whereColumn.length, i = 0; i < whereColCnt; ++i) {
                whereQuery.put((Object)whereColumn[i], (Object)whereValue[i]);
            }
            collection.remove((DBObject)whereQuery);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public void deleteData(final MongoDatabase db, final String tableName, final String[] whereColumn, final String[] whereValue) {
        MongoCollection<Document> table = null;
        Document whereQuery = null;
        try {
            table = (MongoCollection<Document>)db.getCollection(tableName);
            whereQuery = new Document();
            for (int whereColCnt = whereColumn.length, i = 0; i < whereColCnt; ++i) {
                whereQuery.put(whereColumn[i], (Object)whereValue[i]);
            }
            table.deleteOne((Bson)whereQuery);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public LinkedList<Document> selectData(final MongoDatabase db, final String tableName, final String[] whereColumn, final String[] whereValue) {
        MongoCollection<Document> table = null;
        Document whereQuery = null;
        FindIterable<Document> findResult = null;
        LinkedList<Document> result = null;
        try {
            table = (MongoCollection<Document>)db.getCollection(tableName);
            whereQuery = new Document();
            for (int whereColCnt = whereColumn.length, i = 0; i < whereColCnt; ++i) {
                whereQuery.put(whereColumn[i], (Object)whereValue[i]);
            }
            findResult = (FindIterable<Document>)table.find((Bson)whereQuery);
            final Iterator<Document> resultSet = (Iterator<Document>)findResult.iterator();
            if (resultSet != null) {
                result = new LinkedList<Document>();
            }
            while (resultSet.hasNext()) {
                final Document tmpDoc = resultSet.next();
                System.out.println("contain key {name} : " + tmpDoc.containsKey((Object)"name"));
                result.add(tmpDoc);
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public LinkedList<Document> selectData(final MongoDatabase db, final String tableName, final String[] whereColumn, final String[] whereValue, final String orderByColumn, final int orderByValue) {
        MongoCollection<Document> table = null;
        Document whereQuery = null;
        FindIterable<Document> findResult = null;
        LinkedList<Document> result = null;
        try {
            if (db == null || tableName == null || tableName.trim().equals("") || whereColumn == null || whereValue == null || orderByColumn == null || orderByColumn.trim().equals("") || (orderByValue < -1 && orderByValue > 1)) {
                return result;
            }
            table = (MongoCollection<Document>)db.getCollection(tableName);
            whereQuery = new Document();
            for (int whereColCnt = whereColumn.length, i = 0; i < whereColCnt; ++i) {
                whereQuery.put(whereColumn[i], (Object)whereValue[i]);
            }
            findResult = (FindIterable<Document>)table.find((Bson)whereQuery).sort((Bson)new Document(orderByColumn, (Object)orderByValue));
            final Iterator<Document> resultSet = (Iterator<Document>)findResult.iterator();
            if (resultSet != null) {
                result = new LinkedList<Document>();
            }
            while (resultSet.hasNext()) {
                final Document tmpDoc = resultSet.next();
                result.add(tmpDoc);
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static void main(final String[] args) {
        final String hostIp = "localhost";
        final int hostPort = 27017;
        final String dbname = "test";
        final String user = "acidhan";
        final String passwd = "rhaepfpffk";
        try {
            LoggerElements.setLevel(4);
            final MongoHandler dbhandler = new MongoHandler();
            System.out.println("connect : " + dbhandler.connect("localhost", 27017, "test", "acidhan", "rhaepfpffk"));
            dbhandler.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
