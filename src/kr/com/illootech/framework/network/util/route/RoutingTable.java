

package kr.com.illootech.framework.network.util.route;

import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import kr.com.illootech.framework.file.log.Logger;
import java.util.LinkedList;

public class RoutingTable
{
    private LinkedList<RoutingDataSet> ROUTING_TABLE;
    
    public RoutingTable() {
        this.ROUTING_TABLE = null;
    }
    
    public boolean init() {
        try {
            this.ROUTING_TABLE = new LinkedList<RoutingDataSet>();
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public synchronized boolean clear() {
        try {
            synchronized (this.ROUTING_TABLE) {
                if (this.ROUTING_TABLE != null) {
                    this.ROUTING_TABLE.clear();
                }
            }
            // monitorexit(this.ROUTING_TABLE)
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public synchronized boolean release() {
        try {
            synchronized (this.ROUTING_TABLE) {
                if (this.ROUTING_TABLE != null) {
                    this.ROUTING_TABLE.clear();
                }
            }
            // monitorexit(this.ROUTING_TABLE = null)
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean put(final String id, final BufferedInputStream in, final BufferedOutputStream out, final Object obj) {
        RoutingDataSet rds = null;
        try {
            rds = new RoutingDataSet();
            rds.put(id, in, out, obj);
            synchronized (this.ROUTING_TABLE) {
                this.ROUTING_TABLE.add(rds);
            }
            // monitorexit(this.ROUTING_TABLE)
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean put(final String id, final BufferedInputStream in, final BufferedOutputStream out) {
        RoutingDataSet rds = null;
        try {
            rds = new RoutingDataSet();
            rds.put(id, in, out);
            synchronized (this.ROUTING_TABLE) {
                this.ROUTING_TABLE.add(rds);
            }
            // monitorexit(this.ROUTING_TABLE)
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean put(final RoutingDataSet set) {
        try {
            synchronized (this.ROUTING_TABLE) {
                this.ROUTING_TABLE.add(set);
            }
            // monitorexit(this.ROUTING_TABLE)
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean remove(final String id, final BufferedInputStream in, final BufferedOutputStream out) {
        RoutingDataSet rds = null;
        try {
            if (id == null || in == null || out == null) {
                return false;
            }
            rds = new RoutingDataSet();
            rds.put(id, in, out);
            return this.remove(rds);
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean remove(final String procId) {
        boolean result = false;
        try {
            synchronized (this.ROUTING_TABLE) {
                final RoutingDataSet rdset = this.getDataSet(procId);
                if (rdset == null) {
                    // monitorexit(this.ROUTING_TABLE)
                    return result;
                }
                this.ROUTING_TABLE.remove(rdset);
                result = true;
            }
            // monitorexit(this.ROUTING_TABLE)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean remove(final RoutingDataSet set) {
        try {
            if (this.ROUTING_TABLE != null && this.ROUTING_TABLE.size() > 0) {
                synchronized (this.ROUTING_TABLE) {
                    if (this.ROUTING_TABLE.remove(set)) {
                        // monitorexit(this.ROUTING_TABLE)
                        return true;
                    }
                    // monitorexit(this.ROUTING_TABLE)
                    return false;
                }
            }
            return false;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public RoutingDataSet poll() {
        RoutingDataSet rds = null;
        try {
            synchronized (this.ROUTING_TABLE) {
                rds = this.ROUTING_TABLE.poll();
            }
            // monitorexit(this.ROUTING_TABLE)
            return rds;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public RoutingDataSet getDataSet(final String id) {
        RoutingDataSet result = null;
        try {
            synchronized (this.ROUTING_TABLE) {
                for (int size = (int)this.size(), i = 0; i < size; ++i) {
                    final RoutingDataSet rdset = this.ROUTING_TABLE.get(i);
                    if (rdset != null) {
                        final String tmpId = rdset.getID();
                        if (tmpId != null) {
                            if (!tmpId.equals("")) {
                                if (id.equals(tmpId)) {
                                    result = rdset;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            // monitorexit(this.ROUTING_TABLE)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public LinkedList<String> getIdList() {
        LinkedList<String> result = null;
        try {
            synchronized (this.ROUTING_TABLE) {
                final int size = (int)this.size();
                if (size > 0) {
                    result = new LinkedList<String>();
                }
                for (int i = 0; i < size; ++i) {
                    final RoutingDataSet rdset = this.ROUTING_TABLE.get(i);
                    if (rdset != null) {
                        final String tmpId = rdset.getID();
                        if (tmpId != null) {
                            if (!tmpId.equals("")) {
                                result.add(tmpId);
                            }
                        }
                    }
                }
            }
            // monitorexit(this.ROUTING_TABLE)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public BufferedInputStream getInputStream(final String id) {
        BufferedInputStream in = null;
        try {
            synchronized (this.ROUTING_TABLE) {
                in = this.ROUTING_TABLE.poll().getInputStream();
            }
            // monitorexit(this.ROUTING_TABLE)
            return in;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public BufferedOutputStream getOutputStream(final String id) {
        BufferedOutputStream out = null;
        try {
            synchronized (this.ROUTING_TABLE) {
                out = this.ROUTING_TABLE.poll().getOutputStream();
            }
            // monitorexit(this.ROUTING_TABLE)
            return out;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public boolean containKey(final RoutingDataSet dataset) {
        try {
            return this.ROUTING_TABLE.contains(dataset);
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public long size() {
        return new Long(this.ROUTING_TABLE.size());
    }
}
