package com.activity.bondprice;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class BondPriceProducer {
    private static final Logger logger = LoggerFactory.getLogger(BondPriceProducer.class);
    private final KafkaProducer<String, String> producer; // ✅ Make producer final
    private final String topic = "enhanced-bond-prices";

    public BondPriceProducer() {
        Properties props = KafkaConfig.getProducerProperties();
        this.producer = new KafkaProducer<>(props);
    }

    public void send(String key, String enhancedPrice) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, enhancedPrice);

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                logger.error( "Failed to send bond price", exception);
            } else {
                logger.info("Published bond price -> Topic: {}, Partition: {}, Offset: {}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }

    // close producer to release resources
    public void close() {
        producer.close();
        logger.info("Kafka producer closed.");
    }
}
