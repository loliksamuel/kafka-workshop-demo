package demo.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import kafka.javaapi.consumer.SimpleConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AppMainConsumerString {

    private SimpleConsumer simpleConsumer;

    private final DemoConsumerJson consumer;


    private final static  String TOPIC_NAME = "testString";

    public static void main(String[] args) throws JsonProcessingException {

        AppMainConsumerString.readAllRecordsManualOffsetControl(TOPIC_NAME);
        //AppMainConsumerJson.partitionsAssignments();
        //DemoConsumerJson.readAllRecords();

    }

    public AppMainConsumerString() {
        List<String> topics = Collections.singletonList(TOPIC_NAME);
        consumer = new DemoConsumerJson(topics);

    }







    public static void readAllRecordsManualOffsetControl(String topicName) {
        List<String> topics = Collections.singletonList(topicName);
        DemoConsumerString consumer = new DemoConsumerString(topics);

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll();
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println("topic:" + record.topic() +", partition:" + record.partition() +", offset:" + record.offset() + " : msg=" + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commit(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }
    }

    public static void partitionsAssignments() {
        DemoConsumerJson consumer = new DemoConsumerJson(null);

        TopicPartition partition0 = new TopicPartition(TOPIC_NAME, 0);
        TopicPartition partition1 = new TopicPartition(TOPIC_NAME, 1);
        consumer.assign(Arrays.asList(partition0, partition1));
    }
}
