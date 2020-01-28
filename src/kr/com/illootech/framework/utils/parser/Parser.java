

package kr.com.illootech.framework.utils.parser;

import kr.com.illootech.framework.file.log.Logger;
import java.util.LinkedList;

public class Parser
{
    public LinkedList<String> parse(final String data, final String delimiter, final String defaultValue, final boolean trim) {
        LinkedList<String> result = null;
        int totalSize = 0;
        int startPos = 0;
        int endPos = 0;
        int index = 0;
        int delimiterSize = 0;
        try {
            totalSize = data.length();
            if (totalSize > 0) {
                result = new LinkedList<String>();
            }
            delimiterSize = delimiter.length();
            while (startPos < totalSize) {
                String tmpStr = null;
                index = data.indexOf(delimiter, startPos);
                if (index < 0) {
                    tmpStr = data.substring(startPos, totalSize);
                    if (tmpStr == null) {
                        tmpStr = defaultValue;
                    }
                    if (trim) {
                        tmpStr = tmpStr.trim();
                    }
                    result.add(tmpStr);
                    break;
                }
                if (startPos == index) {
                    ++startPos;
                }
                else {
                    endPos = index;
                    tmpStr = data.substring(startPos, endPos);
                    if (tmpStr == null) {
                        tmpStr = defaultValue;
                    }
                    if (trim) {
                        tmpStr = tmpStr.trim();
                    }
                    result.add(tmpStr);
                    startPos = endPos + delimiterSize;
                }
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public LinkedList<String> parse(final String data, final String delimiter, final boolean trim) {
        LinkedList<String> result = null;
        int totalSize = 0;
        int startPos = 0;
        int endPos = 0;
        int index = 0;
        int delimiterSize = 0;
        try {
            totalSize = data.length();
            if (totalSize > 0) {
                result = new LinkedList<String>();
            }
            delimiterSize = delimiter.length();
            while (startPos < totalSize) {
                String tmpStr = null;
                index = data.indexOf(delimiter, startPos);
                if (index < 0) {
                    tmpStr = data.substring(startPos, totalSize);
                    if (tmpStr == null) {
                        return null;
                    }
                    if (trim) {
                        tmpStr = tmpStr.trim();
                    }
                    result.add(tmpStr);
                    break;
                }
                else if (startPos == index) {
                    ++startPos;
                }
                else {
                    endPos = index;
                    tmpStr = data.substring(startPos, endPos);
                    if (tmpStr == null) {
                        return null;
                    }
                    if (trim) {
                        tmpStr = tmpStr.trim();
                    }
                    result.add(tmpStr);
                    startPos = endPos + delimiterSize;
                }
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public void parse(final LinkedList<String> output, String input, final LinkedList<String> delimiters) {
        int index = 0;
        int startPos = 0;
        int endPos = 0;
        String fTag = null;
        String tTag = null;
        String result = null;
        try {
            if (input == null || delimiters == null) {
                return;
            }
            if (delimiters.size() > 0) {
                fTag = delimiters.pollFirst();
                index = delimiters.indexOf(fTag);
                if (index < 0) {
                    return;
                }
                startPos = index;
                if (delimiters.size() == 0) {
                    endPos = input.length();
                }
                else {
                    tTag = delimiters.get(0);
                    index = input.indexOf(tTag, startPos + 1);
                    if (index < 0) {
                        endPos = input.length();
                    }
                    else {
                        endPos = index;
                    }
                }
                result = input.substring(startPos, endPos);
                if (result == null) {
                    return;
                }
                result = result.trim();
                output.add(result);
                input = input.substring(endPos, input.length());
                this.parse(output, input, delimiters);
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public static void main(final String[] args) {
        final Parser tp = new Parser();
        tp.parse("$,$;", "$", true);
        tp.parse("A=a, B=b, C=c", ", ", true);
        tp.parse("A,B, ", ",", "#", true);
    }
}
