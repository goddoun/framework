

package kr.com.illootech.framework.databases.rdbms.oracle.sql.statement;

import java.sql.CallableStatement;
import java.sql.SQLException;
import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import java.sql.Connection;
import java.sql.PreparedStatement;
import kr.com.illootech.framework.databases.IFSQL;

public class PreparedStmt implements IFSQL
{
    private final String procname = "PREPARED.STMT";
    private PreparedStatement pstmt;
    private String query;
    
    public PreparedStmt() {
        this.pstmt = null;
        this.query = null;
    }
    
    @Override
    public boolean init(final Connection conn, final String query) {
        boolean result = false;
        try {
            if (conn == null || query == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. prepared.statement. DBSession or Query is not available] - FAIL", "PREPARED.STMT", "ERROR"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            this.query = query;
            this.pstmt = conn.prepareStatement(this.query);
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. prepared.statement. Query '%3$s'] - %4$s", "PREPARED.STMT", result ? "RUNNING" : "ERROR", (this.query != null) ? this.query : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    @Override
    public void release() {
        try {
            if (this.pstmt != null) {
                this.pstmt.close();
            }
        }
        catch (SQLException se) {
            Logger.error(se);
            return;
        }
        finally {
            this.pstmt = null;
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Released. prepared.statement. Query '%3$s']", "PREPARED.STMT", "RUNNING", (this.query != null) ? this.query : "N/A"), LoggerElements.LOG_LEVEL3);
        }
        this.pstmt = null;
        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Released. prepared.statement. Query '%3$s']", "PREPARED.STMT", "RUNNING", (this.query != null) ? this.query : "N/A"), LoggerElements.LOG_LEVEL3);
    }
    
    @Override
    public PreparedStatement getPreparedStatement() {
        return this.pstmt;
    }
    
    public String getQuery() {
        return this.query;
    }
    
    @Override
    public CallableStatement getCallableStatement() {
        return null;
    }
}
