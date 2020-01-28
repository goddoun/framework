

package kr.com.illootech.framework.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;

import kr.com.illootech.framework.file.log.Logger;

public class FileIOHandler
{
    public String makeFilename(String path, final String filename) {
        final String TAG_OSNAME = "os.name";
        final String OSNM_WIN = "Windows";
        final String SEP_WIN = "\\";
        final String SEP_UNX = "/";
        try {
            if (path == null) {
                return null;
            }
            final StringBuffer sb = new StringBuffer();
            sb.append(path);
            final String osname = System.getProperty("os.name");
            if (osname == null) {
                return null;
            }
            if (osname.indexOf("Windows") >= 0) {
                if (!sb.toString().endsWith("\\")) {
                    sb.append("\\");
                }
            }
            else if (!sb.toString().endsWith("/")) {
                sb.append("/");
            }
            path = sb.toString();
            final File dir = new File(sb.toString());
            if (!dir.exists()) {
                dir.mkdir();
            }
            sb.append(filename);
            return sb.toString();
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public boolean exists(final String file) {
        try {
            final File tmpFile = new File(file);
            return tmpFile.exists();
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean isReadable(final String file) {
        try {
            final File tmpFile = new File(file);
            return tmpFile.canRead();
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public LinkedList<String> getFilenameList(final String path) {
        LinkedList<String> fileList = null;
        File[] tmpList = null;
        int listLength = 0;
        File dir = null;
        try {
            dir = new File(path);
            if (!dir.isDirectory()) {
                return fileList;
            }
            tmpList = dir.listFiles();
            if (tmpList == null) {
                return fileList;
            }
            listLength = tmpList.length;
            fileList = new LinkedList<String>();
            for (int i = 0; i < listLength; ++i) {
                if (tmpList[i].isFile()) {
                    fileList.add(tmpList[i].getName());
                }
            }
            return fileList;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            dir = null;
            tmpList = null;
        }
    }
    
    public ArrayList<String> getFilenames(final String path) {
        ArrayList<String> result = null;
        try {
            result = this.getFilenames(path, null);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public ArrayList<String> getFilenames(final String path, final String filenamePrefix) {
        ArrayList<String> result = null;
        String[] filenameList = null;
        File dir = null;
        try {
            dir = new File(path);
            if (!dir.exists()) {
                return result;
            }
            filenameList = dir.list();
            if (filenameList == null) {
                return result;
            }
            result = new ArrayList<String>();
            for (final String filename : filenameList) {
                Label_0110: {
                    if (filenamePrefix == null || filenamePrefix.equals("")) {
                        result.add(filename);
                    }
                    else if (!filename.startsWith(filenamePrefix)) {
                        break Label_0110;
                    }
                    result.add(filename);
                }
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public LinkedList<String> getFilenameList(final String path, final String filePrefix) {
        LinkedList<String> fileList = null;
        File[] tmpList = null;
        int listLength = 0;
        File dir = null;
        try {
            dir = new File(path);
            if (!dir.exists()) {
                return fileList;
            }
            tmpList = dir.listFiles();
            if (tmpList == null) {
                return null;
            }
            listLength = tmpList.length;
            fileList = new LinkedList<String>();
            for (int i = 0; i < listLength; ++i) {
                if (tmpList[i].isFile() && tmpList[i].getName().startsWith(filePrefix)) {
                    fileList.add(tmpList[i].getName());
                }
            }
            return fileList;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            dir = null;
            tmpList = null;
        }
    }
    
    public boolean rename(final String src, final String dest) {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        FileChannel channelIn = null;
        FileChannel channelOut = null;
        try {
            fin = new FileInputStream(src);
            fout = new FileOutputStream(dest);
            channelIn = fin.getChannel();
            channelOut = fout.getChannel();
            channelIn.transferTo(0L, channelIn.size(), channelOut);
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            try {
                if (channelIn != null) {
                    channelIn.close();
                }
                channelIn = null;
                if (channelOut != null) {
                    channelOut.close();
                }
                channelOut = null;
                if (fin != null) {
                    fin.close();
                }
                fin = null;
                if (fout != null) {
                    fout.close();
                }
                fout = null;
            }
            catch (IOException ie) {
                Logger.error(ie);
            }
        }
    }
    
    public boolean delete(final String src) {
        boolean result = false;
        File srcFile = null;
        try {
            srcFile = new File(src);
            if (!srcFile.exists()) {
                return result;
            }
            result = srcFile.delete();
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            srcFile = null;
        }
    }
    
    public long size(final String file) {
        final long result = 0L;
        File src = null;
        try {
            src = new File(file);
            if (!src.exists()) {
                return result;
            }
            return src.length();
        }
        catch (Exception e) {
            Logger.error(e);
            return 0L;
        }
    }
    
    public Object read(final String file, final Object src, final Object dest) {
        ObjectInputStream inputStream = null;
        Object retObj = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
            retObj = new Object();
            retObj = inputStream.readObject();
            return retObj;
        }
        catch (IOException ie) {
            Logger.error(ie);
            return null;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                inputStream = null;
            }
            catch (IOException ie2) {
                Logger.error(ie2);
                return null;
            }
        }
    }
    
    public byte[] read(final String file) {
        FileChannel fch = null;
        int bufLen = 0;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            fch = raf.getChannel();
            bufLen = (int)raf.length();
            final ByteBuffer bbuf = ByteBuffer.allocate(bufLen);
            fch.read(bbuf);
            return bbuf.array();
        }
        catch (IOException ie) {
            Logger.error(ie);
            return null;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            try {
                if (fch != null) {
                    fch.close();
                }
                if (raf != null) {
                    raf.close();
                }
                fch = null;
                raf = null;
            }
            catch (IOException ie2) {
                Logger.error(ie2);
            }
        }
    }
    
    public byte[] read(final String file, final int offset) {
        FileChannel fch = null;
        int bufLen = 0;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            fch = raf.getChannel();
            fch.position(offset);
            bufLen = (int)raf.length() - offset;
            final ByteBuffer bbuf = ByteBuffer.allocate(bufLen);
            fch.read(bbuf);
            return bbuf.array();
        }
        catch (IOException ie) {
            Logger.error(ie);
            return null;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            try {
                if (fch != null) {
                    fch.close();
                }
                if (raf != null) {
                    raf.close();
                }
                fch = null;
                raf = null;
            }
            catch (IOException ie2) {
                Logger.error(ie2);
            }
        }
    }
    
    public byte[] read(final String file, final int offset, final int len) {
        FileChannel fch = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            fch = raf.getChannel();
            fch.position(offset);
            final ByteBuffer bbuf = ByteBuffer.allocate(len);
            fch.read(bbuf);
            return bbuf.array();
        }
        catch (IOException ie) {
            Logger.error(ie);
            return null;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            try {
                if (fch != null) {
                    fch.close();
                }
                if (raf != null) {
                    raf.close();
                }
                fch = null;
                raf = null;
            }
            catch (IOException ie2) {
                Logger.error(ie2);
            }
        }
    }
    
    public LinkedList<String> readLine(final String file) {
        String tmpStr = null;
        File inFile = null;
        FileReader freader = null;
        BufferedReader breader = null;
        LinkedList<String> resultDataList = new LinkedList<String>();
        try {
            inFile = new File(file);
            if (!inFile.exists()) {
                return null;
            }
            freader = new FileReader(file);
            breader = new BufferedReader(freader);
            while ((tmpStr = breader.readLine()) != null) {
                resultDataList.add(tmpStr);
            }
            return resultDataList;
        }
        catch (IOException ie) {
            Logger.error(ie);
            return null;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            try {
                resultDataList = null;
                if (breader != null) {
                    breader.close();
                }
                breader = null;
                if (freader != null) {
                    freader.close();
                }
                freader = null;
            }
            catch (IOException ie2) {
                Logger.error(ie2);
                return null;
            }
        }
    }
    
    public synchronized void write(final String file, final Object src) {
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(src);
        }
        catch (IOException ie) {
            Logger.error(ie);
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                outputStream = null;
            }
            catch (IOException ie2) {
                Logger.error(ie2);
            }
        }
    }
    
    public synchronized void write(final String file, final byte[] data, final int offset, final int len, final boolean append) {
        FileChannel fch = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            fch = raf.getChannel();
            if (append) {
                raf.seek(raf.length());
            }
            final ByteBuffer bb = ByteBuffer.wrap(data, offset, len);
            synchronized (fch) {
                fch.write(bb);
            }
            // monitorexit(fch)
        }
        catch (IOException ie) {
            Logger.error(ie);
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            try {
                if (fch != null) {
                    fch.close();
                }
                fch = null;
                if (raf != null) {
                    raf.close();
                }
                raf = null;
            }
            catch (IOException ie2) {
                Logger.error(ie2);
            }
        }
        try {
            if (fch != null) {
                fch.close();
            }
            fch = null;
            if (raf != null) {
                raf.close();
            }
            raf = null;
        }
        catch (IOException ie2) {
            Logger.error(ie2);
        }
    }
    
    public synchronized void writeLine(final String file, final byte[] data, final int offset, final int len, final boolean append) {
        FileChannel fch = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            fch = raf.getChannel();
            if (append) {
                raf.seek(raf.length());
            }
            final ByteBuffer body = ByteBuffer.wrap(data, offset, len);
            final ByteBuffer cr = ByteBuffer.wrap("\r".getBytes());
            synchronized (fch) {
                fch.write(body);
                fch.write(cr);
            }
            // monitorexit(fch)
        }
        catch (IOException ie) {
            Logger.error(ie);
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            try {
                if (fch != null) {
                    fch.close();
                }
                fch = null;
                if (raf != null) {
                    raf.close();
                }
                raf = null;
            }
            catch (IOException ie2) {
                Logger.error(ie2);
            }
        }
        try {
            if (fch != null) {
                fch.close();
            }
            fch = null;
            if (raf != null) {
                raf.close();
            }
            raf = null;
        }
        catch (IOException ie2) {
            Logger.error(ie2);
        }
    }
}
