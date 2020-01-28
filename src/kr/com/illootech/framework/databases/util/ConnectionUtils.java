

package kr.com.illootech.framework.databases.util;

import java.util.LinkedList;

import kr.com.illootech.framework.file.log.Logger;

public class ConnectionUtils
{
    public LinkedList<String> parseIpList(final String ip) {
        final LinkedList<String> result = null;
        try {
            if (ip == null || ip.trim().equals("")) {
                return result;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
}
