

package kr.com.illootech.framework.databases;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Connection;

public interface IFSQL
{
    boolean init(final Connection p0, final String p1);
    
    void release();
    
    PreparedStatement getPreparedStatement();
    
    CallableStatement getCallableStatement();
}
