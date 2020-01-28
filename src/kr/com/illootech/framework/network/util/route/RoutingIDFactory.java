

package kr.com.illootech.framework.network.util.route;

import kr.com.illootech.framework.file.log.Logger;

public class RoutingIDFactory
{
    private int routingTag;
    
    public RoutingIDFactory() {
        this.routingTag = 0;
    }
    
    public synchronized boolean init() {
        try {
            this.routingTag = 0;
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public synchronized int get() {
        try {
            if (++this.routingTag >= 9999) {
                this.routingTag = 0;
            }
            return this.routingTag;
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }
    
    public synchronized String get(final int digit) {
        try {
            if (digit <= 0) {
                return null;
            }
            if (++this.routingTag >= 9999) {
                this.routingTag = 0;
            }
            return String.format("%1$0" + digit + "d", this.routingTag);
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static void main(final String[] args) {
        final RoutingIDFactory nid = new RoutingIDFactory();
        System.out.println(nid.get());
        for (int i = 0; i < 50; ++i) {
            System.out.println(nid.get(i));
        }
    }
}
