package com.activity.bondprice;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class EnhancedBondPriceConsumer {
    private static final Logger logger = LoggerFactory.getLogger(EnhancedBondPriceConsumer.class);
    private final KafkaConsumer<String, String> enhancedConsumer;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON Parser

    public EnhancedBondPriceConsumer() {
        Properties props = KafkaConfig.getEnhancedConsumerProperties();
        this.enhancedConsumer = new KafkaConsumer<>(props);
        this.enhancedConsumer.subscribe(Collections.singletonList("enhanced-bond-prices"));
    }

    public ArrayList<EnhancedBondPriceBean> getEnhancedBeans() {
        System.out.println("Starting the Enhanced consumer");
        ArrayList<EnhancedBondPriceBean> enhancedBonds = new ArrayList<>();

        try {
            ConsumerRecords<String, String> records = enhancedConsumer.poll(Duration.ofMillis(5000));
            for (ConsumerRecord<String, String> record : records) {
                try {
                    logger.info("Received bond price -> Key: {}, Value: {}", record.key(), record.value());

                    // Parse JSON
                    JsonNode jsonNode = objectMapper.readTree(record.value());
                    String bond = jsonNode.get("bond").asText();
                    String originalPrice = jsonNode.get("originalPrice").asText();
                    String enhancedPrice = jsonNode.get("enhancedPrice").asText();

                    // Create Bean
                    EnhancedBondPriceBean tempBean = new EnhancedBondPriceBean(bond, originalPrice, enhancedPrice);
                    enhancedBonds.add(tempBean);

                    // Commit offset after successful processing
                    enhancedConsumer.commitSync(Collections.singletonMap(
                            new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1)
                    ));
                } catch (Exception e) {
                    logger.error("Error processing bond price for value: {}, sending to DLQ", record.value(), e);
                }
            }
        } catch (Exception e) {
            logger.error("Consumer encountered an error", e);
        } finally {
            System.out.println("Consumer closed");
        }
        return enhancedBonds;
    }
}

