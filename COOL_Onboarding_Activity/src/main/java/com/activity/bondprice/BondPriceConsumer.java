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
    private BondPriceProducer producer; //producer for publishing enhanced prices

    public BondPriceConsumer(BondPriceProducer producer) { // producer in constructor
        Properties props = KafkaConfig.getConsumerProperties();
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("bond-prices"));
        this.producer = producer; // ✅ Assign producer
    }

    public void consume(BondPriceProcessor processor) {
        while (true) {

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("Received raw bond price message: Key = {}, Value = {}", record.key(), record.value());
                logger.info("Received bond price -> Key: {}, Value: {}", record.key(), record.value());

                //Process bond price to generate enhanced bond price
                String enhancedPrice = processor.enhanceBondPrice(record.key(), record.value());

                //Send enhanced price to the next Kafka topic (enhanced-bond-prices)
                producer.send(record.key(), enhancedPrice);
            }
        }
    }
}
