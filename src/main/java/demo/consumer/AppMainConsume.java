package demo.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;

public class AppMainConsume {

    public static void main(String[] args) throws JsonProcessingException {

        DemoConsumerSimple.readAllRecordsManualOffsetControl();
        //DemoConsumerSimple.partitionsAssignments();
        //DemoConsumer.readAllRecords();

    }
}
