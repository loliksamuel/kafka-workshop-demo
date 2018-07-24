package demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import demo.consumer.DemoConsumer;
import demo.consumer.DemoSimpleConsumer;

public class CustomerRunner {

    public static void main(String[] args) throws JsonProcessingException {

        DemoSimpleConsumer.readAllRecordsManualOffsetControl();
        DemoSimpleConsumer.partitionsAssignments();
        DemoConsumer.readAllRecords();

    }
}
