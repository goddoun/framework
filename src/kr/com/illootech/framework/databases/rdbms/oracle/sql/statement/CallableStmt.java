

package kr.com.illootech.framework.databases.rdbms.oracle.sql.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import java.sql.Connection;
import java.sql.CallableStatement;
import kr.com.illootech.framework.databases.IFSQL;

public class CallableStmt implements IFSQL
{
    private final String procname = "CALLABLE.STMT";
    private CallableStatement cstmt;
    private String query;
    
    public CallableStmt() {
        this.cstmt = null;
        this.query = null;
    }
    
    @Override
    public boolean init(final Connection conn, final String query) {
        boolean result = false;
        try {
            if (conn == null || query == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. prepared.statement. DBSession or Query is not available] - FAIL", "CALLABLE.STMT", "ERROR"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            this.query = query;
            this.cstmt = conn.prepareCall(this.query);
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. callable.statement. Query '%3$s'] - %4$s", "CALLABLE.STMT", result ? "RUNNING" : "ERROR", (this.query != null) ? this.query : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    @Override
    public void release() {
        try {
            if (this.cstmt != null) {
                this.cstmt.close();
            }
        }
        catch (SQLException se) {
            Logger.error(se);
            return;
        }
        finally {
            this.cstmt = null;
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Released. prepared.statement. Query '%3$s']", "CALLABLE.STMT", "RUNNING", (this.query != null) ? this.query : "N/A"), LoggerElements.LOG_LEVEL3);
        }
        this.cstmt = null;
        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Released. prepared.statement. Query '%3$s']", "CALLABLE.STMT", "RUNNING", (this.query != null) ? this.query : "N/A"), LoggerElements.LOG_LEVEL3);
    }
    
    @Override
    public CallableStatement getCallableStatement() {
        return this.cstmt;
    }
    
    @Override
    public PreparedStatement getPreparedStatement() {
        return null;
    }
}
