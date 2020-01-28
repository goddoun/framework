

package kr.com.illootech.framework.file.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.com.illootech.framework.file.log.test.TestThread;

public class Logger
{
    private static File file;
    private static RandomAccessFile raf;
    private static FileChannel fch;
    private static long filePosition;
    
    static 
    {
        Logger.file = null;
        Logger.raf = null;
        Logger.fch = null;
        Logger.filePosition = 0L;
    }
    
    private static boolean initFile() 
    {
        boolean result = false;
        String filename = null;
        try {
            filename = createFileName(".yyyyMMdd.");
            if (filename == null || filename.trim().equals("")) {
                filename = LoggerElements.defaultSystemLogFileName;
            }
            Logger.file = new File(filename);
            Logger.raf = new RandomAccessFile(Logger.file, "rw");
            Logger.fch = Logger.raf.getChannel();
            if (!Logger.file.exists()) {
                Logger.filePosition = 0L;
            }
            else {
                Logger.filePosition = Logger.fch.size();
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            error(e);
            return false;
        }
    }
    
    private static String createFileName(final String simpleDateFormat) 
    {
        String result = null;
        Calendar currentDate = null;
        StringBuffer tmpFilename = null;
        try {
            currentDate = Calendar.getInstance();
            final DateFormat df = new SimpleDateFormat(simpleDateFormat);
            tmpFilename = new StringBuffer(LoggerElements.defaultSystemLogFileName);
            tmpFilename.append(df.format(currentDate.getTime()));
            tmpFilename.append(LoggerElements.getFourDigitsFileIndex());
            tmpFilename.append(".log");
            result = tmpFilename.toString();
            return result;
        }
        catch (Exception e) {
            error(e);
            return null;
        }
    }
    
    private static String createFileName() 
    {
        String result = null;
        Calendar currentDate = null;
        StringBuffer tmpFilename = null;
        try {
            currentDate = Calendar.getInstance();
            final DateFormat df = new SimpleDateFormat(".yyyyMMdd.HH.");
            tmpFilename = new StringBuffer(LoggerElements.defaultSystemLogFileName);
            tmpFilename.append(df.format(currentDate.getTime()));
            tmpFilename.append(LoggerElements.getFourDigitsFileIndex());
            tmpFilename.append(".log");
            result = tmpFilename.toString();
            return result;
        }
        catch (Exception e) {
            error(e);
            return null;
        }
    }
    
    private static void close() {
        try {
            if (Logger.fch != null) {
                Logger.fch.close();
            }
            if (Logger.raf != null) {
                Logger.raf.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Logger.fch = null;
            Logger.raf = null;
            Logger.file = null;
        }
    }
    
    private static void write(final String msg, final int level) {
        StringBuffer logStr = null;
        Timestamp ts = null;
        try {
            if (LoggerElements.getLevel() >= level) {
                ts = new Timestamp(System.currentTimeMillis());
                logStr = new StringBuffer(String.format("[%1$tY-%1$tm-%1$td]-[%1$tH:%1$tM:%1$tS:%1$tL] - ", ts));
                logStr.append(msg);
                logStr.append("\r\n");
                final ByteBuffer bb = ByteBuffer.wrap(logStr.toString().getBytes());
                Logger.raf.seek(Logger.filePosition);
                synchronized (Logger.fch) {
                    System.out.print(logStr.toString());
                    Logger.fch.write(bb);
                    Logger.filePosition = Logger.raf.getFilePointer();
                }
                // monitorexit(Logger.fch)
            }
        }
        catch (IOException ie) {
            close();
        }
        catch (Exception e) {
            close();
        }
    }
    
    public static synchronized void sysInfo(final String msg, final int level) {
        try {
            if (!LoggerElements.isLogon()) {
                return;
            }
            if (!initFile()) {
                return;
            }
            write(msg, level);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
    }
    
    public static synchronized void error(final Exception msg) {
        FileOutputStream fos = null;
        String filename = null;
        try {
            if (LoggerElements.isLogon()) {
                if (msg != null) {
                    filename = createFileName();
                    fos = new FileOutputStream(filename, true);
                    synchronized (fos) {
                        msg.printStackTrace();
                        msg.printStackTrace(new PrintStream(fos));
                    }
                    // monitorexit(fos)
                    final StackTraceElement[] errorElements = msg.getStackTrace();
                    for (int elementsCnt = errorElements.length, i = 0; i < elementsCnt; ++i) {
                        final String className = errorElements[i].getClassName();
                        final String methodName = errorElements[i].getMethodName();
                        final int lineNum = errorElements[i].getLineNumber();
                        final String reason = msg.getMessage();
                        final String tmpStr = String.format("[%1$-20s][%2$-10s][Class '%3$s'. Method '%4$s'. ERR.Line '%5$d'. Reason '%6$s]", "PLATFORM", "EXCEPTION", className, methodName, lineNum, reason);
                        write(tmpStr, LoggerElements.getLevel());
                    }
                    return;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                fos = null;
            }
            catch (IOException ie) {
                ie.printStackTrace();
            }
        }
    }
    
    public static void main(final String[] args) {
        final int threadCnt = 10;
        final String log = "./TEST";
        try {
            final TestThread[] threads = new TestThread[threadCnt];
            for (int i = 0; i < threadCnt; ++i) {
                threads[i] = new TestThread("./TEST", i);
            }
            for (int i = 0; i < threadCnt; ++i) {
                threads[i].startup("TEST." + i);
            }
        }
        catch (Exception e) {
            error(e);
        }
    }
}
