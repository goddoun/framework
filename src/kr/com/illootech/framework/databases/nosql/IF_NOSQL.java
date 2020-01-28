

package kr.com.illootech.framework.databases.nosql;

import com.mongodb.Mongo;

public interface IF_NOSQL
{
    boolean connect(final String p0, final int p1, final String p2);
    
    void disconnect();
    
    boolean authenticate(final String p0, final String p1);
    
    Mongo getSetssion();
}
