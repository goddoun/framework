

package kr.com.illootech.framework.databases;

import java.sql.Connection;

public interface IFConnectionPool
{
    boolean init();
    
    void release();
    
    boolean put(final Connection p0);
    
    Connection poll();
    
    Connection get(final int p0);
    
    int size();
}
