package com.activity.bondprice;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Properties;

public class InitBondProducer{
    private static final Logger logger = LoggerFactory.getLogger(InitBondProducer.class);
    private KafkaProducer<String, String> producer;
    private final String topic = "bond-prices"; // Topic to publish to
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON Serializer

    public InitBondProducer() {
        Properties props = KafkaConfig.getProducerProperties();
        producer = new KafkaProducer<>(props);
    }

    public void produce() {
        try {
            // Ensure price is a number (not a string)
            String usBondPrice = objectMapper.writeValueAsString(Map.of("bond", "US1025", "price", 150.0));
            String inBondPrice = objectMapper.writeValueAsString(Map.of("bond", "IN4096", "price", 100.0));
            String jpBondPrice = objectMapper.writeValueAsString(Map.of("bond", "JP1331", "price", 125.0));

            //Send JSON messages
            producer.send(new ProducerRecord<>(topic, "US1025", usBondPrice));
            producer.send(new ProducerRecord<>(topic, "IN4096", inBondPrice));
            producer.send(new ProducerRecord<>(topic, "JP1331", jpBondPrice));

            logger.info("Sent bond prices to Kafka topic: {}", topic);

            producer.flush();
            producer.close();
        } catch (Exception e) {
            logger.error("Error sending bond prices", e);
        }
    }
}
