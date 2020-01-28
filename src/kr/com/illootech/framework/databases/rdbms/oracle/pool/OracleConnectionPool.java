

package kr.com.illootech.framework.databases.rdbms.oracle.pool;

import kr.com.illootech.framework.file.log.Logger;
import java.sql.Connection;
import java.util.LinkedList;
import kr.com.illootech.framework.databases.IFConnectionPool;

public class OracleConnectionPool implements IFConnectionPool
{
    private int poolSize;
    private LinkedList<Connection> pool;
    
    public OracleConnectionPool() {
        this.poolSize = 0;
        this.pool = null;
    }
    
    @Override
    public boolean init() {
        boolean result = false;
        try {
            if (this.pool == null) {
                this.pool = new LinkedList<Connection>();
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    @Override
    public void release() {
        try {
            if (this.pool != null) {
                for (int poolSize = this.size(), i = 0; i < poolSize; ++i) {
                    final Connection conn = this.pool.get(i);
                    if (conn != null) {
                        conn.close();
                    }
                }
                this.pool.clear();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.poolSize = -1;
            this.pool = null;
        }
    }
    
    @Override
    public synchronized boolean put(Connection conn) {
        boolean result = false;
        try {
            if (conn == null) {
                return result;
            }
            if (this.pool == null) {
                if (conn != null) {
                    conn.close();
                }
                conn = null;
                result = true;
            }
            else {
                synchronized (this.pool) {
                    this.pool.add(conn);
                    this.poolSize = this.pool.size();
                    result = true;
                }
                // monitorexit(this.pool)
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    @Override
    public synchronized Connection poll() {
        Connection result = null;
        try {
            if (this.pool == null || this.poolSize <= 0) {
                return result;
            }
            synchronized (this.pool) {
                result = this.pool.pollFirst();
                this.poolSize = this.pool.size();
            }
            // monitorexit(this.pool)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    @Override
    public Connection get(final int index) {
        Connection result = null;
        try {
            if (this.pool == null || this.poolSize <= 0) {
                return result;
            }
            synchronized (this.pool) {
                result = this.pool.get(index);
            }
            // monitorexit(this.pool)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    @Override
    public int size() {
        return this.poolSize;
    }
}
