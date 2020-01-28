

package kr.com.illootech.framework.databases.rdbms.mariadb.handler;

import java.sql.ResultSet;
import java.sql.Statement;
import kr.com.illootech.framework.file.log.LoggerElements;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.LinkedList;
import kr.com.illootech.framework.utils.parser.Parser;
import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.databases.rdbms.mariadb.driver.MariaDBDriver;
import java.sql.Connection;
import kr.com.illootech.framework.databases.IFDBDriver;

public class MariaDBHandler
{
    protected final String procname = "MARIA.HDL";
    protected String[] defaultDBInfo;
    protected String defaultDBUser;
    protected String defaultDBPasswd;
    protected String MARIADB;
    protected IFDBDriver m_driver;
    protected Connection conn;
    
    public MariaDBHandler() {
        this.defaultDBInfo = null;
        this.defaultDBUser = null;
        this.defaultDBPasswd = null;
        this.MARIADB = "jdbc:mariadb://";
        this.m_driver = new MariaDBDriver();
        this.conn = null;
    }
    
    public boolean init(final String dbIps, final int dbPorts, final String SID, final String userID, final String passwd) {
        boolean result = false;
        String tmpStr = null;
        try {
            tmpStr = String.valueOf(dbPorts);
            result = this.init(dbIps, tmpStr, SID, userID, passwd);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean init(final String dbIps, final String dbPorts, final String dbName, final String userID, final String passwd) {
        final String DELIM = ",";
        boolean result = false;
        Parser parser = null;
        LinkedList<String> tmpDbIpInfo = null;
        LinkedList<String> tmpDbPortInfo = null;
        String[] ip = null;
        int[] port = null;
        try {
            if (dbIps == null || dbPorts == null || dbName == null || userID == null || passwd == null) {
                return result;
            }
            parser = new Parser();
            tmpDbIpInfo = parser.parse(dbIps, ",", true);
            if (tmpDbIpInfo == null) {
                return result;
            }
            tmpDbPortInfo = parser.parse(dbPorts, ",", true);
            if (tmpDbPortInfo == null) {
                return result;
            }
            final int dbIpInfoCnt = tmpDbIpInfo.size();
            final int dbPortInfoCnt = tmpDbPortInfo.size();
            if (dbIpInfoCnt != dbPortInfoCnt) {
                return result;
            }
            ip = new String[dbIpInfoCnt];
            port = new int[dbPortInfoCnt];
            for (int i = 0; i < dbIpInfoCnt; ++i) {
                final String tmpIp = tmpDbIpInfo.get(i);
                if (tmpIp != null) {
                    final String tmpPort = tmpDbPortInfo.get(i);
                    if (tmpPort == null) {
                        return result;
                    }
                    ip[i] = tmpIp;
                    port[i] = Integer.parseInt(tmpPort);
                }
            }
            result = this.setDbConnInfo(ip, port, dbName, userID, passwd);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    private boolean setDbConnInfo(final String[] ip, final int[] port, final String dbname, final String userID, final String passwd) {
        boolean result = false;
        int ipCnt = -1;
        try {
            ipCnt = ip.length;
            this.defaultDBInfo = new String[ipCnt];
            for (int i = 0; i < ipCnt; ++i) {
                this.defaultDBInfo[i] = String.valueOf(ip[i]) + ":" + port[i] + "/" + dbname;
            }
            this.defaultDBUser = userID;
            this.defaultDBPasswd = passwd;
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean connect() {
        int dbInfoCnt = 0;
        try {
            if (!this.m_driver.load()) {
                return false;
            }
            dbInfoCnt = this.defaultDBInfo.length;
            for (int i = 0; i < dbInfoCnt; ++i) {
                try {
                    this.conn = DriverManager.getConnection(String.valueOf(this.MARIADB) + this.defaultDBInfo[i], this.defaultDBUser, this.defaultDBPasswd);
                }
                catch (SQLException se) {
                    Logger.error(se);
                }
                if (this.conn != null) {
                    Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Connect to mariadb. Idx '%3$d' '%4$s'] - SEUCCESS", "MARIA.HDL", "RUNNING", i, (this.defaultDBInfo[i] != null) ? this.defaultDBInfo[i] : "N/A"), LoggerElements.LOG_LEVEL1);
                    return true;
                }
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Connect to mariadb. Idx '%3$d' '%4$s'] - %5$s", "MARIA.HDL", "RUNNING", i, (this.defaultDBInfo[i] != null) ? this.defaultDBInfo[i] : "N/A", "RETRY"), LoggerElements.LOG_LEVEL1);
            }
            return false;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean disconnect() {
        try {
            if (this.conn != null) {
                this.conn.close();
            }
            return true;
        }
        catch (SQLException se) {
            Logger.error(se);
            return false;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            this.conn = null;
        }
    }
    
    public Connection getConn() {
        try {
            return this.conn;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public int isConnect() {
        final int SUCC = 1;
        final int FAIL = 0;
        Statement stmt = null;
        ResultSet rs = null;
        final String query = "show full processlist";
        try {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                return 1;
            }
            return 0;
        }
        catch (SQLException se) {
            Logger.error(se);
            return -se.getErrorCode();
        }
        catch (Exception e) {
            Logger.error(e);
            return 0;
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                if (stmt != null) {
                    stmt.close();
                }
                stmt = null;
            }
            catch (SQLException se2) {
                Logger.error(se2);
            }
        }
    }
    
    public static void main(final String[] args) {
        MariaDBHandler m = null;
        try {
            m = new MariaDBHandler();
            m.init("illootech2.iptime.org, illootech.iptime.org", "1111,3306", "bluewalnut", "fds", "illootechrnd");
            System.out.println("is connect : " + m.connect());
            m.isConnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            m.disconnect();
        }
    }
}
