

package kr.com.illootech.framework.network.util.filter;

import kr.com.illootech.framework.file.log.Logger;
import java.util.LinkedList;

public class RCFilter
{
    private LinkedList<String> filteringList;
    
    public RCFilter() {
        this.filteringList = new LinkedList<String>();
    }
    
    public void Add(final String element) {
        try {
            if (this.filteringList == null) {
                this.filteringList = new LinkedList<String>();
            }
            if (element == null) {
                return;
            }
            this.filteringList.add(element);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public void Add(final LinkedList<String> element) {
        try {
            if (element == null) {
                return;
            }
            this.filteringList = element;
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public LinkedList<String> Get() {
        try {
            return this.filteringList;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public void Clear() {
        try {
            this.filteringList.clear();
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public int Size() {
        try {
            if (this.filteringList != null) {
                return this.filteringList.size();
            }
            return -1;
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }
    
    public boolean IsContained(final String element) {
        try {
            return this.filteringList != null && this.filteringList.size() > 0 && this.filteringList.contains(element);
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
}
