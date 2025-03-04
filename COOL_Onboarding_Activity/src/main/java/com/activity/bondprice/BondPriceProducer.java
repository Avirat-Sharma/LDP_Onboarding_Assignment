package com.activity.bondprice;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class BondPriceProducer {
    private static final Logger logger = LoggerFactory.getLogger(BondPriceProducer.class);
    private KafkaProducer<String, String> producer;
    private final String topic = "enhanced-bond-prices"; // Ensure correct topic

    public BondPriceProducer() {
        Properties props = KafkaConfig.getProducerProperties();
        producer = new KafkaProducer<>(props);
    }

    public void send(String key, String enhancedPrice) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, enhancedPrice);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                logger.error("Failed to send enhanced bond price", exception);
            } else {
                logger.info("Published enhanced bond price -> Key: {}, Value: {}", key, enhancedPrice);
            }
        });
    }
}
