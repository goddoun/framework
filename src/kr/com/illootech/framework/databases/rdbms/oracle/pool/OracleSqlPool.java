

package kr.com.illootech.framework.databases.rdbms.oracle.pool;

import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.databases.IFSQLDataSet;
import java.util.LinkedList;
import kr.com.illootech.framework.databases.IFSQLPool;

public class OracleSqlPool implements IFSQLPool
{
    private LinkedList<IFSQLDataSet> POOL;
    
    public OracleSqlPool() {
        this.POOL = null;
        this.POOL = new LinkedList<IFSQLDataSet>();
    }
    
    @Override
    public synchronized boolean put(final IFSQLDataSet dataset) {
        boolean result = false;
        try {
            if (dataset == null) {
                return result;
            }
            synchronized (this.POOL) {
                result = this.POOL.add(dataset);
            }
            // monitorexit(this.POOL)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    @Override
    public synchronized IFSQLDataSet poll() {
        IFSQLDataSet result = null;
        try {
            synchronized (this.POOL) {
                result = this.POOL.pollFirst();
            }
            // monitorexit(this.POOL)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    @Override
    public int size() {
        return this.POOL.size();
    }
}
