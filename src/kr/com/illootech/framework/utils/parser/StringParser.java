

package kr.com.illootech.framework.utils.parser;

import kr.com.illootech.framework.file.log.Logger;
import java.util.StringTokenizer;
import java.util.LinkedList;

public class StringParser
{
    public LinkedList<String> parseStr(final String str, final String delemeter) {
        LinkedList<String> result = null;
        try {
            if (str != null) {
                result = new LinkedList<String>();
                final StringTokenizer st = new StringTokenizer(str, delemeter);
                while (st.hasMoreElements()) {
                    result.add(st.nextToken());
                }
                return result;
            }
            return null;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
}
