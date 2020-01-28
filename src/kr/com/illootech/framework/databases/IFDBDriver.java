

package kr.com.illootech.framework.databases;

import java.sql.Driver;

public interface IFDBDriver
{
    boolean load();
    
    void release();
    
    Driver getDriver();
}
