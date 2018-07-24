package demo.connector.sink;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DemoFileStreamSinkConnector extends SinkConnector {
    static final String FILE_CONFIG = "file";

    private String filename;

    @Override
    public String version() {
        return AppInfoParser.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        filename = props.get(FILE_CONFIG);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return DemoFileStreamSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList<Map<String, String>> configs = new ArrayList<>();
        for (int i = 0; i < maxTasks; i++) {
            Map<String, String> config = new HashMap<>();
            if (filename != null)
                config.put(FILE_CONFIG, filename);
            configs.add(config);
        }
        return configs;
    }

    @Override
    public void stop() {
        // Nothing to do since DemoFileStreamSinkConnector has no background monitoring.
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef()
                .define(FILE_CONFIG,
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        "Source filename. If not specified, the standard input will be used"
                );
    }
}
