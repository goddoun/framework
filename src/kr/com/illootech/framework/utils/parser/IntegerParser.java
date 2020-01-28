

package kr.com.illootech.framework.utils.parser;

public class IntegerParser
{
    public int[] parsingInt(final int arg, final int digit) {
        int[] result = null;
        try {
            final String tmpStr = String.format("%1$0" + digit + "d", arg);
            final int argCnt = tmpStr.getBytes().length;
            result = new int[argCnt];
            for (int i = argCnt; i > 0; --i) {
                try {
                    final char tmpChar = tmpStr.charAt(i - 1);
                    result[i - 1] = Integer.valueOf(String.valueOf(tmpChar));
                }
                catch (NumberFormatException nfe) {
                    return null;
                }
            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public int[] parsingInt(final int arg) {
        int[] result = null;
        try {
            final String tmpStr = String.valueOf(arg);
            final int argCnt = tmpStr.getBytes().length;
            result = new int[argCnt];
            for (int i = argCnt; i > 0; --i) {
                try {
                    final char tmpChar = tmpStr.charAt(i - 1);
                    result[i - 1] = Integer.valueOf(String.valueOf(tmpChar));
                }
                catch (NumberFormatException nfe) {
                    return null;
                }
            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
