

package kr.com.illootech.framework.algorithm.sort;

import kr.com.illootech.framework.file.log.Logger;

public class BubbleSort
{
    public void sort(final Object[] data) {
        int inputSize = 0;
        try {
            if (data == null) {
                return;
            }
            inputSize = data.length;
            for (int i = 0; i < inputSize; ++i) {
                for (int j = 0; j < inputSize - i; ++j) {}
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
}
