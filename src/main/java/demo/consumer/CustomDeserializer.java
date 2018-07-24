package demo.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class CustomDeserializer implements Deserializer<JsonNode> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public JsonNode deserialize(String topic, byte[] data) {
        if (data == null)
            return null;
        else {

            ByteArrayInputStream bis = new ByteArrayInputStream(data);

            try {

                return mapper.readValue(bis, JsonNode.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void close() {

    }
}
