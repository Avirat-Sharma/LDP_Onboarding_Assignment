package com.activity.bondprice;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class BondPriceConsumer {
    private static final Logger logger = LoggerFactory.getLogger(BondPriceConsumer.class);
    private KafkaConsumer<String, String> consumer;

    public BondPriceConsumer() {
        Properties props = KafkaConfig.getConsumerProperties();
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("bond-prices"));
    }

    public void consume(BondPriceProcessor processor, BondPriceProducer producer) {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("Received bond price: {}", record.value());
                String enhancedPrice = processor.enhanceBondPrice(record.value());
                producer.send(enhancedPrice);
            }
        }
    }
}
