

package kr.com.illootech.framework.file.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import kr.com.illootech.framework.file.log.Logger;

public class ConfigLoader
{
    private Properties gwProperty;
    private File srcFile;
    private FileInputStream fis;
    
    public ConfigLoader(final String file) {
        this.srcFile = null;
        try {
            this.srcFile = new File(file);
            this.fis = new FileInputStream(this.srcFile);
            (this.gwProperty = new Properties()).load(this.fis);
        }
        catch (FileNotFoundException fnfe) {
            Logger.error(fnfe);
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            if (this.fis != null) {
                try {
                    this.fis.close();
                    this.fis = null;
                }
                catch (IOException ie) {
                    Logger.error(ie);
                }
            }
        }
        if (this.fis != null) {
            try {
                this.fis.close();
                this.fis = null;
            }
            catch (IOException ie) {
                Logger.error(ie);
            }
        }
    }
    
    public String getStringTag(String tag) {
        final String result = null;
        try {
            if (tag == null) {
                return result;
            }
            tag = tag.trim();
            if (tag.equals("")) {
                return result;
            }
            return this.gwProperty.getProperty(tag);
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public int getIntTag(String tag) {
        final int result = -1;
        try {
            if (tag == null) {
                return result;
            }
            tag = tag.trim();
            if (tag.equals("")) {
                return result;
            }
            return Integer.parseInt(this.gwProperty.getProperty(tag));
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }
}
