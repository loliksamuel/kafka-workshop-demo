package demo.producer;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.producer.DemoProducer;

import java.io.IOException;
import java.util.UUID;

public class AppMainProduce {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("run producer & consumer ");
        System.out.println("u will see how kafka send the msg to different machines(partitions) in the distributed cluster");
        System.out.println("in order to reduce load from 1 machine ");
        DemoProducer producer = new DemoProducer();
        ObjectMapper mapper   = new ObjectMapper();
        String       msgStr ;
        JsonNode     msgParsed;
        final String TOPIC_NAME="test";

        for (int i = 0; i < 1000; i++) {
            msgStr =  "{" + "\"id\": \"" + UUID.randomUUID().toString()
                           + "\"," +  "\"timestamp\": " + System.currentTimeMillis() +  "}";
            System.out.println("sending :"+msgStr);
            msgParsed =  mapper.valueToTree(msgStr);
            producer.sendMessage(TOPIC_NAME, msgParsed);
            Thread.sleep(2000);
        }
        producer.close();

    }
}
