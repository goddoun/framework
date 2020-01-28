

package kr.com.illootech.framework.network.util.filter.ip;

import java.util.List;
import java.io.IOException;
import kr.com.illootech.framework.file.log.Logger;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import kr.com.illootech.framework.network.util.filter.RCFilter;

public class IPFilter extends RCFilter
{
    public int LoadIPList(final String file) {
        int result = -1;
        FileReader fReader = null;
        BufferedReader brInput = null;
        String contents = null;
        int count = 0;
        try {
            fReader = new FileReader(file);
            brInput = new BufferedReader(fReader);
            while ((contents = brInput.readLine()) != null) {
                if (!contents.equals("") && !contents.equals(" ")) {
                    this.Add(contents.trim());
                    ++count;
                }
            }
            result = count;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
        finally {
            try {
                if (brInput != null) {
                    brInput.close();
                }
                brInput = null;
                if (fReader != null) {
                    fReader.close();
                }
                fReader = null;
            }
            catch (IOException ie) {
                Logger.error(ie);
            }
        }
    }
    
    public List<String> GetIPList() {
        try {
            return this.Get();
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public void ClearIPList() {
        try {
            this.Clear();
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public int IPListSize() {
        try {
            return this.Size();
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }
    
    public boolean IsRegisteredIPAddress(final String element) {
        try {
            return this.IPListSize() <= 0 || this.IsContained(element);
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
}
