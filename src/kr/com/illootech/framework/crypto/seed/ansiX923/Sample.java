

package kr.com.illootech.framework.crypto.seed.ansiX923;

import kr.com.illootech.framework.utils.operator.ByteOperator;

public class Sample
{
    final String DEFAULT_KEY = "I1a4T1c2N5I7P2T3";
    
    private void Test() {
        try {
            String text = "123456789012345";
            text = text.toUpperCase();
            final SeedCipher seedEnc = new SeedCipher();
            final byte[] resultSeedEnc = seedEnc.encrypt(text, "I1a4T1c2N5I7P2T3".getBytes(), "UTF-8");
            System.out.println("Start encrypt >> ");
            System.out.println("Input >> [" + text + "]");
            System.out.println("Output >> [" + ByteOperator.ByteToHexStr(resultSeedEnc, 0, resultSeedEnc.length, "/") + "][" + text.getBytes("UTF-8").length + " ---> " + resultSeedEnc.length + " bytes ]");
            System.out.println("\n");
            final SeedCipher seedDec = new SeedCipher();
            final byte[] resultSeedDec = seedDec.decrypt(resultSeedEnc, "I1a4T1c2N5I7P2T3".getBytes("UTF-8"));
            System.out.println("Start decrypt >> ");
            System.out.println("Input >> [" + ByteOperator.ByteToHexStr(resultSeedEnc, 0, resultSeedEnc.length, "/") + "]");
            System.out.println("Output >> [" + new String(resultSeedDec, 0, resultSeedDec.length, "UTF-8") + "][" + resultSeedEnc.length + "--->" + resultSeedDec.length + " bytes ]");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final Sample sample = new Sample();
        sample.Test();
    }
}
