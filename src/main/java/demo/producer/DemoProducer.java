package demo.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DemoProducer {


    private final KafkaProducer<String, JsonNode> producer;
    private final Callback callback;

    public DemoProducer() {

        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG       , "localhost:9090,localhost:9091,localhost:9092");//must
        props.put(ProducerConfig.CLIENT_ID_CONFIG               , "demo-producer");
        props.put(ProducerConfig.ACKS_CONFIG                    , "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG    , StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG  , CustomSerializer.class.getName());
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG       , CustomPartitioner.class.getCanonicalName());

        this.producer = new KafkaProducer<>(props);

        this.callback = (metadata, exception) -> {
            if (exception == null) {
                System.out.println( "topic :"    + metadata.topic()
                                + ", offset:"    + metadata.offset()
                                + ", partition:" + metadata.partition());
            } else {
                System.out.println(exception.getMessage());
            }
        };
    }

    public void sendMessage(String topic, JsonNode msg) {

        producer.send(new ProducerRecord<>(topic, msg), callback);
    }



    public void close() {
        producer.close();
    }
}
