

package kr.com.illootech.framework.utils;

import java.sql.Timestamp;

public class Time
{
    public static String get() {
        try {
            return get("");
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String get(final String delimeter) {
        try {
            final Timestamp ts = new Timestamp(System.currentTimeMillis());
            return String.format("%1$tH" + delimeter + "%1$tM" + delimeter + "%1$tS" + delimeter + "%1$tL", ts);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getHH() {
        try {
            final Timestamp ts = new Timestamp(System.currentTimeMillis());
            return String.format("%1$tH", ts);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getMM() {
        try {
            final Timestamp ts = new Timestamp(System.currentTimeMillis());
            return String.format("%1$tM", ts);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getSec() {
        try {
            final Timestamp ts = new Timestamp(System.currentTimeMillis());
            return String.format("%1$tS", ts);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getMilliSec() {
        try {
            final Timestamp ts = new Timestamp(System.currentTimeMillis());
            return String.format("%1$tL", ts);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
