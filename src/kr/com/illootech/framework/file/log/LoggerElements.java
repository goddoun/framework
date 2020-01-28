

package kr.com.illootech.framework.file.log;

import java.math.BigDecimal;
import java.util.LinkedList;

public class LoggerElements
{
    public static String defaultSystemLogPath;
    public static String defaultSystemLogFilePrefix;
    public static String defaultSystemLogFileName;
    public static int LOG_LEVEL0;
    public static int LOG_LEVEL1;
    public static int LOG_LEVEL2;
    public static int LOG_LEVEL3;
    public static int LOG_LEVEL4;
    public static String defaultPacketDumpPath;
    public static String defaultPacketDumpFilePrefix;
    public static String defaultPacketDumpFileName;
    private static int level;
    private static boolean LOG_TURNON;
    private static String filenameIndexer;
    private static LinkedList<String> filenameList;
    private static int fileCountLimit;
    private static BigDecimal fileMBSizeLimit;
    
    static {
        LoggerElements.defaultSystemLogPath = "./";
        LoggerElements.defaultSystemLogFilePrefix = "CAF.DEFAULT.SYSLOG";
        LoggerElements.defaultSystemLogFileName = "./CAF.DEFAULT.SYSLOG";
        LoggerElements.LOG_LEVEL0 = 0;
        LoggerElements.LOG_LEVEL1 = 1;
        LoggerElements.LOG_LEVEL2 = 2;
        LoggerElements.LOG_LEVEL3 = 3;
        LoggerElements.LOG_LEVEL4 = 4;
        LoggerElements.defaultPacketDumpPath = "./";
        LoggerElements.defaultPacketDumpFilePrefix = "PACKET_DUMP_";
        LoggerElements.defaultPacketDumpFileName = "./PACKET_DUMP_";
        LoggerElements.level = LoggerElements.LOG_LEVEL1;
        LoggerElements.LOG_TURNON = true;
        LoggerElements.filenameIndexer = "0";
        LoggerElements.filenameList = null;
        LoggerElements.fileCountLimit = 0;
        LoggerElements.fileMBSizeLimit = null;
    }
    
    public static void setLevel(final int level) {
        LoggerElements.level = level;
    }
    
    public static int getLevel() {
        return LoggerElements.level;
    }
    
    public static void setLogon(final boolean on) {
        LoggerElements.LOG_TURNON = on;
    }
    
    public static boolean isLogon() {
        return LoggerElements.LOG_TURNON;
    }
    
    public static void setFourDigitsFileIndex(int index) {
        try {
            if (index < 0) {
                index = 0;
            }
            LoggerElements.filenameIndexer = String.format("%1$04d", index);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public static String getFourDigitsFileIndex() {
        String result = null;
        int tmpIndex = 0;
        try {
            tmpIndex = Integer.parseInt(LoggerElements.filenameIndexer);
            if (tmpIndex >= 9999) {
                tmpIndex = 0;
            }
            result = String.format("%1$04d", tmpIndex);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static void setFileSizeLimit(final BigDecimal size) {
        try {
            LoggerElements.fileMBSizeLimit = size;
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public static BigDecimal getFileSizeLimit() {
        return LoggerElements.fileMBSizeLimit;
    }
    
    public static void setFileCountLimit(final int count) {
        LoggerElements.fileCountLimit = count;
    }
    
    public static int getFileCountLimit() {
        return LoggerElements.fileCountLimit;
    }
    
    public static boolean setLogFileToManagementList(final String filename) {
        boolean result = false;
        try {
            if (filename == null) {
                return result;
            }
            if (LoggerElements.filenameList == null) {
                (LoggerElements.filenameList = new LinkedList<String>()).add(filename);
            }
            else {
                LoggerElements.filenameList.addLast(filename);
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public static LinkedList<String> getLogFileFromManagementList() {
        return LoggerElements.filenameList;
    }
    
    public static void main(final String[] args) {
        try {
            setFileSizeLimit(new BigDecimal(1024));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
