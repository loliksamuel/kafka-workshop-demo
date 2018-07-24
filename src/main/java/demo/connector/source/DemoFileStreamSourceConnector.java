package demo.connector.source;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DemoFileStreamSourceConnector extends SourceConnector {

    static final String TOPIC_CONFIG = "topic";
    static final String FILE_CONFIG = "file";

    private String filename;
    private String topic;

    @Override
    public String version() {
        return AppInfoParser.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        filename = props.get(FILE_CONFIG);
        topic = props.get(TOPIC_CONFIG);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return DemoFileStreamSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList<Map<String, String>> configs = new ArrayList<>();
        Map<String, String> config = new HashMap<>();
        if (filename != null)
            config.put(FILE_CONFIG, filename);
        config.put(TOPIC_CONFIG, topic);
        configs.add(config);
        return configs;
    }

    @Override
    public void stop() {
        // Nothing to do since DemoFileStreamSourceConnector has no background monitoring.
    }

    @Override
    public ConfigDef config() {

        return new ConfigDef()
                .define(FILE_CONFIG,
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        "Source filename. If not specified, the standard input will be used"
                )
                .define(
                        TOPIC_CONFIG,
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        "The topic to publish data to"
                );
    }
}
