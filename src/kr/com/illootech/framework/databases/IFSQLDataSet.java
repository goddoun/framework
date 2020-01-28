

package kr.com.illootech.framework.databases;

import java.sql.Connection;

public interface IFSQLDataSet
{
    boolean setQuery(final String p0, final IFSQL p1);
    
    IFSQL getQuery(final String p0);
    
    void setConn(final Connection p0);
    
    Connection getConn();
    
    void release();
}
