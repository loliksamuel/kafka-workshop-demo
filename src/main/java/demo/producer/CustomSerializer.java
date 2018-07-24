package demo.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class CustomSerializer implements Serializer<JsonNode> {

    private final ObjectWriter writer = new ObjectMapper().writer();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, JsonNode data) {

        try {
            return writer.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    @Override
    public void close() {

    }
}
