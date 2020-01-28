

package kr.com.illootech.framework.databases;

public interface IFSQLPool
{
    boolean put(final IFSQLDataSet p0);
    
    IFSQLDataSet poll();
    
    int size();
}
