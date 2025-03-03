package com.activity.bondprice;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class BondPriceProducer {
    private static final Logger logger = LoggerFactory.getLogger(BondPriceProducer.class);
    private KafkaProducer<String, String> producer;

    public BondPriceProducer() {
        Properties props = KafkaConfig.getProducerProperties();
        producer = new KafkaProducer<>(props);
    }

    public void send(String enhancedPrice) {
        producer.send(new ProducerRecord<>("enhanced-bond-prices", enhancedPrice), (metadata, exception) -> {
            if (exception != null) {
                logger.error("Failed to send message", exception);
            } else {
                logger.info("Published enhanced bond price: {}", enhancedPrice);
            }
        });
    }
}

