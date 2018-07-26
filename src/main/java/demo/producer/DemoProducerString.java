package demo.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class DemoProducerString {


    private final KafkaProducer<String, String> producer;
    private final Callback callback;

    public DemoProducerString() {

        Properties props = new Properties();

        props.put(ProducerConfig.     BOOTSTRAP_SERVERS_CONFIG  , "localhost:9090,localhost:9091,localhost:9092");//must
        props.put(ProducerConfig.             CLIENT_ID_CONFIG  , "demo-producer");
        props.put(ProducerConfig.                  ACKS_CONFIG  , "all");
        props.put(ProducerConfig.     PARTITIONER_CLASS_CONFIG  , PartitionerCustom.class.getCanonicalName());
        props.put(ProducerConfig.  KEY_SERIALIZER_CLASS_CONFIG  , StringSerializer.class.getName());//topic is string
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG  , StringSerializer.class.getName());//msg is json object
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

    public void sendMessage(String topic, String msg) {

        producer.send(new ProducerRecord<>(topic, msg), callback);
    }




    public void close() {
        producer.close();
    }
}