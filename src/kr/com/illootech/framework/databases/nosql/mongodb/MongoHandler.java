

package kr.com.illootech.framework.databases.nosql.mongodb;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;

import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import kr.com.illootech.framework.utils.parser.Parser;

public class MongoHandler
{
    private final String procname = "MONGO.DBH";
    private MongoCredential credential;
    private String mongoConnectionURIStr;
    private MongoClientURI connectinURI;
    private MongoClient clientSession;
    private String dbName;
    private MongoDatabase db;
    private MongoCollection<Document> collection;
    
    public MongoHandler() {
        this.credential = null;
        this.mongoConnectionURIStr = null;
        this.connectinURI = null;
        this.clientSession = null;
        this.dbName = null;
        this.db = null;
        this.collection = null;
    }
    
    public boolean connect(final String hostIp, final int port, final String dbname, final String user, final String passwd) {
        boolean result = false;
        try {
            if (hostIp == null || port <= 0 || dbname == null || user == null || passwd == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Connect with monogdb. Arguments are not available] - FAIL", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            this.dbName = dbname;
            this.mongoConnectionURIStr = String.format("mongodb://%1$s:%2$s@%3$s:%4$d/?authSource=%5$s", user, passwd, hostIp, port, this.dbName);
            this.connectinURI = new MongoClientURI(this.mongoConnectionURIStr);
            this.clientSession = new MongoClient(this.connectinURI);
            if (this.clientSession == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Mongo.client.session is not available] - FAIL", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            this.dbName = dbname;
            this.db = this.clientSession.getDatabase(this.dbName);
            if (this.db == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Mongo.db is not available] - FAIL", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            if (!result) {
                if (this.clientSession != null) {
                    this.clientSession.close();
                }
                this.clientSession = null;
                this.db = null;
            }
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Connect mongodb '%3$s'] - %4$s", "MONGO.DBH", result ? "RUNNING" : "ERROR", (this.mongoConnectionURIStr != null) ? this.mongoConnectionURIStr : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    public boolean connectHA(final String ips, final String ports, final String dbname, final String user, final String passwd) {
        boolean result = false;
        Parser parser = null;
        LinkedList<String> hostIpList = null;
        LinkedList<String> hostPortList = null;
        LinkedList<ServerAddress> addressList = null;
        try {
            if (ips == null || ports == null || dbname == null || user == null || passwd == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Connect.HA. Argument is not available] - FAIL", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            parser = new Parser();
            hostIpList = parser.parse(ips, ",", true);
            if (hostIpList == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Connect.HA. Host.ip.list is not available] - FAIL", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            hostPortList = parser.parse(ports, ",", true);
            if (hostPortList == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Connect.HA. Host.port.list is not available] - FAIL", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            addressList = new LinkedList<ServerAddress>();
            for (int hostCnt = hostIpList.size(), i = 0; i < hostCnt; ++i) {
                final String tmpIp = hostIpList.get(i);
                final String tmpPort = hostPortList.get(i);
                if (tmpIp != null) {
                    if (tmpPort != null) {
                        final ServerAddress address = new ServerAddress(tmpIp, Integer.parseInt(tmpPort));
                        addressList.add(address);
                    }
                }
            }
            result = this.connect(addressList, dbname, user, passwd);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Connect mongodb by HA. Host.ip.list '%3$s'. Host.port.list '%4$s' DB.name '%5$s'] - %6$s", "MONGO.DBH", result ? "RUNNING" : "ERROR", (hostIpList != null) ? hostIpList : "N/A", (hostPortList != null) ? hostPortList.size() : -1, (dbname != null) ? dbname : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    private boolean connect(final LinkedList<ServerAddress> hostList, final String dbname, final String user, final String passwd) {
        boolean result = false;
        try {
            if (hostList == null || dbname == null || user == null || passwd == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Argument is not available] - FAIL", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            this.credential = MongoCredential.createCredential(user, dbname, passwd.toCharArray());
            if (this.credential == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Mongo.credential is not available] - FAIL", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            this.clientSession = new MongoClient((List)hostList, (List)Arrays.asList(this.credential));
            if (this.clientSession == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Mongo.client.session is not available] - FAIL", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            this.dbName = dbname;
            this.db = this.clientSession.getDatabase(this.dbName);
            if (this.db == null || !this.db.getName().equals(this.dbName)) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Mongo.datbase is not available] - FAIL", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean isConnect() {
        final String DEFAULT_COLLECTION_NAME = "session";
        boolean result = false;
        MongoCollection<Document> sessionColl = null;
        try {
            if (this.db == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Check mongodb.session. Mongo.db is not available]", "MONGO.DBH", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            sessionColl = (MongoCollection<Document>)this.db.getCollection("session");
            if (sessionColl.count() <= 0L) {
                this.db.createCollection("session", new CreateCollectionOptions().capped(true).sizeInBytes(1048576L));
                sessionColl = (MongoCollection<Document>)this.db.getCollection("session");
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Check mongodb.session. Create collection for 'session']", "MONGO.DBH", (sessionColl != null) ? "RUNNING" : "ERROR"), LoggerElements.LOG_LEVEL1);
            }
            if (sessionColl.count() > 0L) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            sessionColl = null;
        }
    }
    
    public void disconnect() {
        try {
            if (this.clientSession != null) {
                this.clientSession.close();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.collection = null;
            this.db = null;
            this.clientSession = null;
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Diconnect mongodb]", "MONGO.DBH", "RUNNING"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    public MongoClient getDBSession() {
        return this.clientSession;
    }
    
    public MongoDatabase getDB() {
        return this.db;
    }
    
    public MongoCollection<Document> getCollection(final String collectionName) {
        try {
            if (this.db == null) {
                return this.collection;
            }
            return this.collection = (MongoCollection<Document>)this.db.getCollection(collectionName);
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Get Collection '%3$s'] - %4$s", "MONGO.DBH", (this.collection != null) ? "RUNNING" : "ERROR", (this.collection != null) ? collectionName : "N/A", (this.collection != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    public static void main(final String[] args) {
        try {
            MongoHandler[] h = null;
            final long starttime = System.currentTimeMillis();
            Logger.sysInfo("Start", LoggerElements.LOG_LEVEL1);
            h = new MongoHandler[3];
            for (int i = 0; i < 3; ++i) {
                h[i] = new MongoHandler();
                System.out.println("Connection is " + h[i].connectHA("localhost", "27017", "Test", "fds", "fds"));
            }
            Logger.sysInfo("elapsed time for connection : " + (System.currentTimeMillis() - starttime), LoggerElements.LOG_LEVEL1);
            for (int i = 0; i < 10; ++i) {
                for (int j = 0; j < 3; ++j) {
                    System.out.println("Index : " + i + ", is connecti : " + h[j].isConnect());
                }
                Thread.sleep(500L);
            }
            System.out.println("end");
            for (int i = 0; i < 3; ++i) {
                h[i].disconnect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
