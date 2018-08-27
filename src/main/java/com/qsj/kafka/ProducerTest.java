package com.qsj.kafka;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Lists.newArrayList;

public class ProducerTest {
    public static void main(String[] args) {
        ProducerTest producerTest = new ProducerTest();
        producerTest.execSendMsg();
        ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
    }
    public void  execSendMsg(){
        Properties properties = new Properties();
        properties.put("ack","1");
        properties.put("retries","0");
        properties.put("batch.size",16384);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bootstrap.servers","192.168.12.222:9092");
        properties.put("group.id","cbtest");
        Producer<String,String> producer1 = new KafkaProducer<String, String>(properties);
        String topic = "test";
        for (int i = 0; i < 10; i++) {
            String value = "i'm test"+i;
            ProducerRecord<String,String> record = new ProducerRecord<>(topic,value);
            producer1.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.println("message send to partition " + recordMetadata.partition() + ", offset: " + recordMetadata.offset());
                }
            });
            System.out.println(i+" ----   success");
        }

        System.out.println("send message over");

        producer1.close();
    }
}
