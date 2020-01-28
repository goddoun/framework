

package kr.com.illootech.framework.databases.rdbms.oracle.driver;

import kr.com.illootech.framework.file.log.Logger;
import java.sql.DriverManager;
import java.sql.Driver;
import kr.com.illootech.framework.databases.IFDBDriver;

public class OracleDriver implements IFDBDriver
{
    private static Driver driver;
    private final String DEFAULT_DRIVER_URL = "oracle.jdbc.driver.OracleDriver";
    
    static {
        OracleDriver.driver = null;
    }
    
    @Override
    public boolean load() {
        try {
            OracleDriver.driver = (Driver)Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            if (OracleDriver.driver != null) {
                DriverManager.registerDriver(OracleDriver.driver);
                return true;
            }
            return false;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    @Override
    public void release() {
        try {
            DriverManager.deregisterDriver(OracleDriver.driver);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    @Override
    public Driver getDriver() {
        try {
            return OracleDriver.driver;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
}
