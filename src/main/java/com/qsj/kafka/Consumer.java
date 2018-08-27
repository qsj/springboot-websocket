package com.qsj.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

import static com.google.common.collect.Lists.newArrayList;

public class Consumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("ack","1");
        properties.put("retries","0");
        properties.put("batch.size",16384);
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("bootstrap.servers","192.168.12.222:9092");
        properties.put("group.id","cbtest");
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(newArrayList("test"));
        while (true){
            System.out.println("poll start...");
            ConsumerRecords<String,String> consumerRecords = consumer.poll(100);
            int count = consumerRecords.count();
            System.out.println("the number of topic :"+count);
            for (ConsumerRecord<String,String> consumerRecord:consumerRecords
                 ) {
                System.out.printf("offset = %d, key = %s, value = %s", consumerRecord.offset(), consumerRecord.key(), consumerRecord.value());
            }
        }
    }
}
