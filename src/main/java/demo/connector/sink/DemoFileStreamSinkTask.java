package demo.connector.sink;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;

public class DemoFileStreamSinkTask extends SinkTask {
    private static final Logger log = LoggerFactory.getLogger(DemoFileStreamSinkTask.class);

    private String filename;
    private PrintStream outputStream;

    public DemoFileStreamSinkTask() {
    }


    @Override
    public String version() {
        return new DemoFileStreamSinkConnector().version();
    }

    @Override
    public void start(Map<String, String> props) {
        filename = props.get(DemoFileStreamSinkConnector.FILE_CONFIG);
        if (filename == null) {
            outputStream = System.out;
        } else {
            try {
                outputStream = new PrintStream(new FileOutputStream(filename, true));
            } catch (FileNotFoundException e) {
                throw new ConnectException("Couldn't find or create file for DemoFileStreamSinkTask", e);
            }
        }
    }

    @Override
    public void put(Collection<SinkRecord> sinkRecords) {
        for (SinkRecord record : sinkRecords) {
            log.trace("Writing line to {}: {}", logFilename(), record.value());
            outputStream.println(record.value());
        }
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> offsets) {
        log.trace("Flushing output stream for {}", logFilename());
        outputStream.flush();
    }

    @Override
    public void stop() {
        if (outputStream != System.out)
            outputStream.close();
    }

    private String logFilename() {
        return filename == null ? "stdout" : filename;
    }
}
