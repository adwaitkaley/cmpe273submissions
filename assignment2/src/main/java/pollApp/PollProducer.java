package pollApp;
/*
*
* Created by Adwait on 4/10/2015.
*/

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

public class PollProducer {
    private static Producer<Integer, String> producer;
    private final Properties properties = new Properties();

/*
    public PollProducer() {
        properties.put("metadata.broker.list", "localhost:9092");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        producer = new Producer<>(new ProducerConfig(properties));
    }
*/
    public PollProducer()
    {
        properties.put("metadata.broker.list", "54.149.84.25:9092");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        producer = new Producer<Integer,String>(new ProducerConfig(properties));
    }

 public static void sendMessage(String topic,String msg) {
        new PollProducer();
        /*String topic = "cmpe273-topic";
        String msg = args[1];
        */
        KeyedMessage<Integer, String> data = new KeyedMessage<>(topic, msg);
        producer.send(data);
        producer.close();
    }
}
