# flume-json-interceptor
Apache flume interceptor which tracks the json and pass it different kafka queue based on json input

The idea behind this flume interceptor is to read the input json from the source and pass it on to different kafka queues. Currently flume supports 1 kafka topic at source level and 1 kafka topic at sink level. 
To transfer data from 1 source to multiple different sinks, this interceptor can be used. To do this all messages sent from the source are required to be of specific format. Following shows example -
```
{"event-topic":<<TOPIC-NAME>>, "message":<< ACTUAL-JSON / Message to be passed on the different topics >>}
```

The interceptor will look for ```message``` and pass it to respective topic by reading the topic name as ```event-topic```.

Sample flume config file -
```
flume1.sources  = kafka-source-1
flume1.channels = kafka-channel-1
flume1.sinks    = kafka-sink-1

flume1.sources.kafka-source-1.type = org.apache.flume.source.kafka.KafkaSource
flume1.sources.kafka-source-1.interceptors = i1
flume1.sources.kafka-source-1.interceptors.i1.type = com.tesnik.flume.interceptor.JsonParserInterceptor$Builder
flume1.sources.kafka-source-1.zookeeperConnect = localhost:2181
flume1.sources.kafka-source-1.topic = global-topic
flume1.sources.kafka-source-1.batchSize = 1
flume1.sources.kafka-source-1.channels = kafka-channel-1
flume1.sources.kafka-source-1.groupId = test1

flume1.channels.kafka-channel-1.type   = memory
flume1.sinks.kafka-sink-1.channel = kafka-channel-1
flume1.sinks.kafka-sink-1.type = org.apache.flume.sink.kafka.KafkaSink
flume1.sinks.kafka-sink-1.topic = %{topic}
flume1.sinks.kafka-sink-1.brokerList = localhost:9092
flume1.sinks.kafka-sink-1.batchSize = 1

flume1.channels.kafka-channel-1.capacity = 10000
flume1.channels.kafka-channel-1.transactionCapacity = 1000
```





