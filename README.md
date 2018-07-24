**Description:** 
```
An introduction to Apache Kafka workshop
Kafka overview (capabilities, and major components)
Hands-on part: 
Running Kafka cluster with basic configuration 
Hello world example
Use cases and tools 
```
 
 
Instructions for running
------------------------
1.  Run zookeeper-zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties


3.  Run your connect-file-source plugin
    ```
    $ export CLASSPATH=/path/to/source-connector-implementation.jar
    $ connect-standalone.sh connect-file/connect-standalone.properties  connect-file/connect-file-source.properties
    ```
    
4.  Write stuff to test.txt (that is the that this connector will read from, as configured in connect-file-source.properties)
    ```
    $ echo `date` >> test.txt
    ```
    
5.  Read the data out from the kafka topic named 'test' (that is the that this connector will write to, as configured in connect-file-source.properties)
    ```
    $ kafka-console-consumer.sh  --zookeeper localhost:2181 --topic test
    {"schema":{"type":"string","optional":false},"payload":"Thu Oct 15 23:03:15 PDT 2015"}
    ```

6.  Run your connect-file-sink plugin
    ```
    $ export CLASSPATH=/path/to/sink-connector-implementation.jar
    $ connect-standalone.sh connect-file/connect-standalone.properties  connect-file/connect-file-sink.properties
    ```

7.  Check that the file-sink plugin has written the data to the file
    ```
    $ cat test.sink.txt
    ```

 
Optional
```
https://github.com/yahoo/kafka-manager/releases
Instructions how to install kafka monitor:
Download sources
Install sbt
$./sbt clean dist
Unzip .../kafka-manager-1.3.3.14_zip/target/universal/kafka-manager-1.3.3.14.zip
Chmod +x bin/kafka-monitor
$bin/kafka-monitor
```
Optional: Kafka tool 
```
wget http://www.kafkatool.com/download/kafkatool.sh
```



