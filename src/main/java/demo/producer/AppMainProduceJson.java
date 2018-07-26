package demo.producer;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class AppMainProduceJson {
    private static final String TOPIC_NAME="testJson";


    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("before u start this streaming job, u must do:" +
                "/n1.  start a kafka broker on port 9092 & zookeeper on port 2181 ");
        System.out.println("zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties");
        System.out.println("2. run producer & consumer ");
        System.out.println("u will see how kafka send the msg to different machines(partitions) in the distributed cluster");
        System.out.println("in order to reduce load from 1 machine. topic="+TOPIC_NAME);
        //DemoProducerString producer = new DemoProducerString();
        DemoProducerJson producer = new DemoProducerJson();
        ObjectMapper  mapper   = new ObjectMapper();
        JsonNode      msgParsed;

        String       msgStr ;

        int rand = 0;
        for (int i = 0; i < 1000; i++) {
            Random r = new Random();
            rand= r.nextInt(10);

            msgStr =  "{" + "\"id\": \""
                    +  UUID.randomUUID().toString()
                    + "\""
                    +"," +  "\"timestamp\": " + System.currentTimeMillis()
                    +  "}";
            System.out.println("sending :"+msgStr);
            msgParsed =  mapper.valueToTree(msgStr);
            producer.sendMessage(TOPIC_NAME, msgParsed);
            //producer.sendMessage(TOPIC_NAME, msgStr);

            Thread.sleep(2000);
        }
        producer.close();

    }
}