package demo;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.producer.DemoProducer;

import java.io.IOException;
import java.util.UUID;

public class ProducerRunner {
    public static void main(String[] args) throws IOException, InterruptedException {

        DemoProducer producer = new DemoProducer();
        ObjectMapper mapper   = new ObjectMapper();

        String content = "{" +
                "\"id\": \"" + UUID.randomUUID().toString() + "\"," +
                "\"timestamp\": " + System.currentTimeMillis() +
                "}";

        final JsonNode msg = mapper.valueToTree(content);

        for (int i = 0; i < 100; i++) {
            producer.sendMessage("test", msg);
            Thread.sleep(100);
        }
        producer.close();

    }
}
