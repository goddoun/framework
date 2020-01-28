

package kr.com.illootech.framework.utils.parser;

import kr.com.illootech.framework.file.log.Logger;
import java.util.LinkedList;
import kr.com.illootech.framework.file.FileIOHandler;

public class XMLParser
{
    final String DEFAULT_START_TAG = "<";
    final String DEFAULT_END_TAG = ">";
    private FileIOHandler fhandler;
    
    public XMLParser() {
        this.fhandler = null;
    }
    
    public LinkedList<String> parsingFileData(final String tag, final String xmlFile, final String charset) {
        LinkedList<String> result = null;
        byte[] fileData = null;
        try {
            this.fhandler = new FileIOHandler();
            fileData = this.fhandler.read(xmlFile);
            if (fileData == null) {
                return result;
            }
            final String tmpStr = new String(fileData, charset).trim();
            if (tmpStr.equals("")) {
                return result;
            }
            final String startTag = String.valueOf("<") + tag + ">";
            final String endTag = String.valueOf("<") + "/" + tag + ">";
            result = new LinkedList<String>();
            int index = 0;
            while ((index = tmpStr.indexOf(startTag, index)) >= 0) {
                final int startPos = index + startTag.length();
                final int endPos = tmpStr.indexOf(endTag, startPos);
                final String pickupStr = tmpStr.substring(startPos, endPos);
                index = endPos;
                result.add(pickupStr);
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public LinkedList<String> parsingStr(final String tag, final String xmlData) {
        LinkedList<String> result = null;
        try {
            final String startTag = String.valueOf("<") + tag + ">";
            final String endTag = String.valueOf("<") + "/" + tag + ">";
            result = new LinkedList<String>();
            int index = 0;
            while ((index = xmlData.indexOf(startTag, index)) >= 0) {
                final int startPos = index + startTag.length();
                final int endPos = xmlData.indexOf(endTag, startPos);
                final String pickupStr = xmlData.substring(startPos, endPos);
                index = endPos;
                result.add(pickupStr);
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static void main(final String[] args) {
    }
}
