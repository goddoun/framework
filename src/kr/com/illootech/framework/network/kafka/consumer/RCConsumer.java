

package kr.com.illootech.framework.network.kafka.consumer;

import java.util.LinkedList;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import kr.com.illootech.framework.thread.RCThread;

public class RCConsumer extends RCThread
{
    private final String KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    private final String VALUE_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    private Properties configs;
    private KafkaConsumer<String, String> consumer;
    private RCConsumerPolicy policy;
    
    public RCConsumer() {
        this.configs = null;
        this.consumer = null;
        this.policy = null;
    }
    
    public boolean init(final String brokerIPAddress, final int brokerPort, final int sessionTimeoutMs, final String consumerGroupName, final String topic, final int partitionNo, final int partitionCnt) {
        boolean result = false;
        try {
            (this.configs = new Properties()).put("bootstrap.servers", String.format("%1$s:%2$d", brokerIPAddress, brokerPort));
            this.configs.put("session.timeout.ms", String.valueOf(sessionTimeoutMs));
            this.configs.put("group.id", consumerGroupName);
            this.configs.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            this.configs.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            this.consumer = (KafkaConsumer<String, String>)new KafkaConsumer(this.configs);
            final LinkedList<String> topics = new LinkedList<String>();
            topics.add(topic);
            this.policy = new RCConsumerPolicy();
            result = this.policy.init((Consumer<?, ?>)this.consumer, topic, partitionNo, partitionCnt);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. Broker's properties. BRK.Address '%3$s:%4$d', G.ID '%5$s', Partition.Info '%6$d / %7$d, Topic '%8$s'] - %9$s", this.getProcname(), result ? "RUNNING" : "ERROR", (brokerIPAddress != null) ? brokerIPAddress : "N/A", brokerPort, (consumerGroupName != null) ? consumerGroupName : "N/A", partitionNo, partitionCnt, (topic != null) ? topic : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    public void release() {
    }
    
    public byte[] recvFromBroker(final String topic) {
        byte[] result = null;
        try {
            final ConsumerRecords<String, String> records = (ConsumerRecords<String, String>)this.consumer.poll(1L);
            if (records == null) {
                return result;
            }
            for (final ConsumerRecord<String, String> record : records) {
                final String readTopic = record.topic();
                if (readTopic != null) {
                    if (readTopic.trim().equals("")) {
                        continue;
                    }
                    if (readTopic.equals(topic)) {
                        result = ((String)record.value()).getBytes();
                        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Receive data from Broker. Type '%3$s', Topic '%4$s', Partition.no '%5$s', Data '%6$s'] - %7$s", this.getProcname(), (result != null) ? "RUNNING" : "ERROR", (readTopic != null) ? readTopic : "N/A", (records != null) ? records.partitions() : "N/A", (record != null) ? record.value() : "N/A", (result != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
                    }
                    else {
                        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Receive data from Broker. Type '%3$s', Topic '%4$s', Not support topic] - DROP", this.getProcname(), "RUNNING", (readTopic != null) ? readTopic : "N/A"), LoggerElements.LOG_LEVEL1);
                    }
                }
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
}
