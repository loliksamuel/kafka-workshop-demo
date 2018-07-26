package demo.producer;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class DemoProducerJson {


    private final KafkaProducer<String, JsonNode> producer;
    private final Callback callback;

    public DemoProducerJson() {

        Properties props = new Properties();

        props.put(ProducerConfig.     BOOTSTRAP_SERVERS_CONFIG  , "localhost:9090,localhost:9091,localhost:9092");//must
        props.put(ProducerConfig.             CLIENT_ID_CONFIG  , "demo-producer");
        props.put(ProducerConfig.                  ACKS_CONFIG  , "all");
        props.put(ProducerConfig.  KEY_SERIALIZER_CLASS_CONFIG  , StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG  , SerializerCustom.class.getName());
        props.put(ProducerConfig.     PARTITIONER_CLASS_CONFIG  , PartitionerCustom.class.getCanonicalName());

        this.producer = new KafkaProducer<>(props);

        this.callback = (metadata, exception) -> {
            if (exception == null) {
                System.out.println( "topic :"    + metadata.topic()
                                + ", partition:" + metadata.partition()
                                + ", offset:"    + metadata.offset()
                        );
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
