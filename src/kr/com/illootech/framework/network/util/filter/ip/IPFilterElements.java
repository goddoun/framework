

package kr.com.illootech.framework.network.util.filter.ip;

public class IPFilterElements
{
    public static String defaultIPRegistryFilePath;
    public static String defaultIPRegistryFilename;
    
    static {
        IPFilterElements.defaultIPRegistryFilePath = "./";
        IPFilterElements.defaultIPRegistryFilename = "IP_Registry.list";
    }
}
