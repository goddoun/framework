

package kr.com.illootech.framework.databases.rdbms.oracle.handler;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.LinkedList;
import kr.com.illootech.framework.utils.parser.Parser;
import kr.com.illootech.framework.file.log.LoggerElements;
import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.databases.rdbms.oracle.driver.OracleDriver;
import java.sql.Connection;
import kr.com.illootech.framework.databases.IFDBDriver;

public class OracleDBHandler
{
    private final String procname = "ORA.HDL";
    protected String[] defaultDBInfoList;
    protected String defaultDBInfoTNSOra;
    protected String defaultDBUser;
    protected String defaultDBPasswd;
    protected String ORACLE;
    protected IFDBDriver m_driver;
    protected Connection m_conn;
    protected String m_procname;
    
    public OracleDBHandler() {
        this.defaultDBInfoList = null;
        this.defaultDBInfoTNSOra = "aaaaa";
        this.defaultDBUser = "abcde";
        this.defaultDBPasswd = "abcde";
        this.ORACLE = "jdbc:oracle:thin:@";
        this.m_driver = new OracleDriver();
        this.m_conn = null;
        this.m_procname = null;
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
    
    public boolean init(final String dbIps, final String dbPorts, final String SID, final String userID, final String passwd) {
        boolean result = false;
        Parser parser = null;
        LinkedList<String> ipList = null;
        LinkedList<String> portList = null;
        try {
            if (dbIps == null || dbPorts == null || SID == null || userID == null || passwd == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. Connection string. Argument is not available] - FAIL", "ORA.HDL", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            parser = new Parser();
            ipList = parser.parse(dbIps, ",", true);
            if (ipList == null) {
                return result;
            }
            portList = parser.parse(dbPorts, ",", true);
            if (portList == null) {
                return result;
            }
            if (ipList.size() != portList.size()) {
                return result;
            }
            final int listSize = ipList.size();
            this.defaultDBInfoList = new String[listSize];
            for (int i = 0; i < listSize; ++i) {
                this.defaultDBInfoList[i] = String.valueOf(ipList.get(i)) + ":" + portList.get(i) + ":" + SID;
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
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. Connection string] - %3$s", "ORA.HDL", result ? "RUNNING" : "ERROR", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    public boolean initTNSOra(final String dbIps, final String dbPorts, final String SID, final String userID, final String passwd) {
        final String DELIM = ",";
        boolean result = false;
        Parser parser = null;
        LinkedList<String> tmpDbIpInfo = null;
        LinkedList<String> tmpDbPortInfo = null;
        String[] ip = null;
        int[] port = null;
        try {
            if (dbIps == null || dbPorts == null || SID == null || userID == null || passwd == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. Connection string by TNS.format. Argument is not available] - FAIL", "ORA.HDL", "ERROR"), LoggerElements.LOG_LEVEL1);
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
            result = this.setDbConnInfo(ip, port, SID, userID, passwd);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init with TNS.format. Connection string] - %3$s", "ORA.HDL", result ? "RUNNING" : "ERROR", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    public boolean connect() {
        boolean result = false;
        try {
            if (!this.m_driver.load()) {
                return result;
            }
            if (this.defaultDBInfoList == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Attempt Connecting to server '%3$s'] - TRY.....", "ORA.HDL", "RUNNING", (this.defaultDBInfoTNSOra != null) ? this.defaultDBInfoTNSOra : "N/A"), LoggerElements.LOG_LEVEL1);
                this.m_conn = DriverManager.getConnection(String.valueOf(this.ORACLE) + this.defaultDBInfoTNSOra, this.defaultDBUser, this.defaultDBPasswd);
                if (this.m_conn != null) {
                    result = true;
                    return result;
                }
            }
            else {
                for (int dbInfoSize = this.defaultDBInfoList.length, i = 0; i < dbInfoSize; ++i) {
                    Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Attempt Connecting to server '%3$s'] - TRY.....", "ORA.HDL", "RUNNING", (this.defaultDBInfoList[i] != null) ? this.defaultDBInfoList[i] : "N/A"), LoggerElements.LOG_LEVEL1);
                    try {
                        this.m_conn = DriverManager.getConnection(String.valueOf(this.ORACLE) + this.defaultDBInfoList[i], this.defaultDBUser, this.defaultDBPasswd);
                    }
                    catch (SQLException e) {
                        Logger.error(e);
                        continue;
                    }
                    if (this.m_conn != null) {
                        result = true;
                        return result;
                    }
                }
            }
            return result;
        }
        catch (SQLException se) {
            Logger.error(se);
            return false;
        }
        catch (Exception e2) {
            Logger.error(e2);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Attempt connecting to server] - %3$s", "ORA.HDL", result ? "RUNNING" : "ERROR", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    private boolean setDbConnInfo(final String[] ip, final int[] port, final String SID, final String userID, final String passwd) {
        boolean result = false;
        StringBuffer sb = null;
        try {
            sb = new StringBuffer();
            sb.append("(DESCRIPTION=");
            sb.append("(FAILOVER=ON)");
            sb.append("(LOAD_BALANCE=ON)");
            sb.append("( ADDRESS_LIST = ");
            for (int cnt = ip.length, i = 0; i < cnt; ++i) {
                sb.append("(ADDRESS = (PROTOCOL = TCP)(HOST = ");
                sb.append(ip[i]);
                sb.append(")(PORT = ");
                sb.append(port[i]);
                sb.append("))");
            }
            sb.append(")");
            sb.append("(CONNECT_DATA=(SERVICE_NAME=");
            sb.append(SID);
            sb.append(")))");
            this.defaultDBInfoTNSOra = sb.toString();
            this.defaultDBUser = userID;
            this.defaultDBPasswd = passwd;
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. Connection string by TNS.format '%3$s'] - %4$s", "ORA.HDL", result ? "RUNNING" : "ERROR", (this.defaultDBInfoTNSOra != null) ? this.defaultDBInfoTNSOra : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    public boolean disconnect() {
        try {
            if (this.m_conn != null) {
                this.m_conn.close();
            }
            this.m_conn = null;
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
            this.m_conn = null;
            this.m_driver = null;
        }
    }
    
    public Connection getConn() {
        return this.m_conn;
    }
    
    public int isConnect() {
        final int SUCC = 1;
        final int FAIL = 0;
        Statement stmt = null;
        ResultSet rs = null;
        final String query = "SELECT * FROM DUAL";
        try {
            stmt = this.m_conn.createStatement();
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
        try {
            final OracleDBHandler db = new OracleDBHandler();
            db.init("192.168.0.14, 192.168.0.5", "1521,1111", "XE", "illootech", "illootechrnd");
            System.out.println("db connect : " + db.connect());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
