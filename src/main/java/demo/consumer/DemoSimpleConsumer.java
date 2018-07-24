package demo.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import kafka.javaapi.consumer.SimpleConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DemoSimpleConsumer {

    private SimpleConsumer simpleConsumer;

    private final DemoConsumer consumer;

    public DemoSimpleConsumer() {
        List<String> topics = Collections.singletonList("test");
        consumer = new DemoConsumer(topics);

    }




    public static void partitionsAssignments() {
        DemoConsumer consumer = new DemoConsumer(null);
        String topic = "test";
        TopicPartition partition0 = new TopicPartition(topic, 0);
        TopicPartition partition1 = new TopicPartition(topic, 1);
        consumer.assign(Arrays.asList(partition0, partition1));
    }


    public static void readAllRecordsManualOffsetControl() {
        List<String> topics = Collections.singletonList("test");
        DemoConsumer consumer = new DemoConsumer(topics);

        try {
            while (true) {
                ConsumerRecords<String, JsonNode> records = consumer.poll();
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, JsonNode>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, JsonNode> record : partitionRecords) {
                        System.out.println(record.offset() + ": " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commit(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }
    }
}
