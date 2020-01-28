

package kr.com.illootech.framework.network.kafka.consumer;

import kr.com.illootech.framework.file.log.Logger;
import java.util.Collection;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import java.util.LinkedList;

public class RCConsumerPolicy
{
    private LinkedList<TopicPartition> partitionList;
    
    public RCConsumerPolicy() {
        this.partitionList = null;
    }
    
    public boolean init(final Consumer<?, ?> consumer, final String topic, final int threadId, final int partitionCnt) {
        boolean result = false;
        int partitionNo = -1;
        try {
            partitionNo = threadId % partitionCnt;
            final TopicPartition partition0 = new TopicPartition(topic, partitionNo);
            (this.partitionList = new LinkedList<TopicPartition>()).add(partition0);
            consumer.assign((Collection)this.partitionList);
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
}
