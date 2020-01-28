

package kr.com.illootech.framework.databases.rdbms.oracle.sql.dataset;

import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.databases.IFSQL;
import java.util.HashMap;
import java.sql.Connection;
import kr.com.illootech.framework.databases.IFSQLDataSet;

public class OracleSqlDataSet implements IFSQLDataSet
{
    private Connection conn;
    private HashMap<String, IFSQL> SQLMap;
    
    public OracleSqlDataSet(final Connection conn) {
        this.conn = null;
        this.SQLMap = null;
        this.conn = conn;
        if (this.SQLMap == null) {
            this.SQLMap = new HashMap<String, IFSQL>();
        }
    }
    
    @Override
    public boolean setQuery(final String id, final IFSQL query) {
        boolean result = false;
        try {
            if (this.SQLMap == null) {
                return result;
            }
            this.SQLMap.put(id, query);
            if (this.SQLMap.containsKey(id)) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    @Override
    public IFSQL getQuery(final String id) {
        IFSQL result = null;
        try {
            if (this.SQLMap == null || id == null) {
                return result;
            }
            result = this.SQLMap.get(id);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    @Override
    public void setConn(final Connection conn) {
        this.conn = conn;
    }
    
    @Override
    public Connection getConn() {
        return this.conn;
    }
    
    @Override
    public void release() {
        try {
            if (this.SQLMap != null) {
                this.SQLMap.clear();
            }
            if (this.conn != null) {
                this.conn.close();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.SQLMap = null;
            this.conn = null;
        }
    }
}
