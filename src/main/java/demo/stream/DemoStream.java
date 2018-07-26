package demo.stream;



import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * https://github.com/confluentinc/examples/blob/3.3.0-post/kafka-streams/src/main/java/io/confluent/examples/streams/WordCountLambdaExample.java
 */
public class DemoStream {



    public static void main(String[] args) {

        final String TOPIC_FROM               = "test";//"TopicWords" "test"//use kafka-console-producer.sh --broker-list localhost:9092 --topic TextLinesTopic
        final String TOPIC_TO                 = "TopicWordsWithCounts";
        final KStreamBuilder    builder       = new KStreamBuilder();
        final Properties streamsConfiguration = getProperties();
        final Serde<String> stringSerde       = Serdes.String();//serelize/deserilized
        final Serde<Long>     longSerde       = Serdes.Long  ();//serelize/deserilized


        final KStream<String, String> textLines = builder.stream(stringSerde, stringSerde, TOPIC_FROM);

        final Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);

        final KTable<String, Long> wordCounts = textLines
                // Split each text line, by whitespace, into words.  The text lines are the record
                // values, i.e. we can ignore whatever data is in the record keys and thus invoke
                // `flatMapValues()` instead of the more generic `flatMap()`.
                .flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
                // Count the occurrences of each word (record key).
                //
                // This will change the stream type from `KStream<String, String>` to `KTable<String, Long>`
                // (word -> count).  In the `count` operation we must provide a name for the resulting KTable,
                // which will be used to name e.g. its associated state store and changelog topic.
                //
                // Note: no need to specify explicit serdes because the resulting key and value types match our default serde settings
                .groupBy((key, word) -> word)
                .count("Counts");
        System.out.println("wordCounts="+wordCounts);

        wordCounts.to(stringSerde, longSerde, TOPIC_TO);


        final KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);

        streams.cleanUp();
        streams.start();
        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static Properties getProperties() {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.          BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamsConfiguration.put(StreamsConfig.             APPLICATION_ID_CONFIG, "wordcount-lambda-example");
        streamsConfiguration.put(StreamsConfig.                  CLIENT_ID_CONFIG, "wordcount-lambda-example-client");
        streamsConfiguration.put(StreamsConfig.         COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        streamsConfiguration.put(StreamsConfig.  CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        streamsConfiguration.put(StreamsConfig.    DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.  DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return streamsConfiguration;
    }


}
