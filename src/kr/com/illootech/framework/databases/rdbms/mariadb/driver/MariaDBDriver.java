

package kr.com.illootech.framework.databases.rdbms.mariadb.driver;

import java.sql.Driver;
import java.sql.DriverManager;

import kr.com.illootech.framework.databases.IFDBDriver;
import kr.com.illootech.framework.file.log.Logger;

public class MariaDBDriver implements IFDBDriver
{
    private static Driver driver;
    private final String DEFAULT_DRIVER_URL = "org.mariadb.jdbc.Driver";
    
    static {
        MariaDBDriver.driver = null;
    }
    
    @Override
    public boolean load() {
        try {
            MariaDBDriver.driver = (Driver)Class.forName("org.mariadb.jdbc.Driver").newInstance();
            if (MariaDBDriver.driver != null) {
                DriverManager.registerDriver(MariaDBDriver.driver);
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
            DriverManager.deregisterDriver(MariaDBDriver.driver);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    @Override
    public Driver getDriver() {
        try {
            return MariaDBDriver.driver;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
}
