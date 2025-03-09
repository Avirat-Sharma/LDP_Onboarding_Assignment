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

public class EnhancedBondPriceConsumer {
    private static final Logger logger = LoggerFactory.getLogger(BondPriceConsumer.class);
    private final KafkaConsumer<String, String> enhancedConsumer;

    public EnhancedBondPriceConsumer() {
        Properties props = KafkaConfig.getEnhancedConsumerProperties();
        this.enhancedConsumer = new KafkaConsumer<>(props);
        this.enhancedConsumer.subscribe(Collections.singletonList("enhanced-bond-prices"));
    }

    public ArrayList<EnhancedBondPriceBean> getEnhancedBeans(){
        ArrayList<EnhancedBondPriceBean> enhancedBonds = new ArrayList<EnhancedBondPriceBean>();

        try {
                ConsumerRecords<String, String> records = enhancedConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        logger.info(" Received bond price -> Key: {}, Value: {}", record.key(), record.value());
                        EnhancedBondPriceBean tempBean = new EnhancedBondPriceBean(record.key(),record.value());
                        enhancedBonds.add(tempBean);
                        //  Commit offset after successful processing
                        enhancedConsumer.commitSync(Collections.singletonMap(
                                new TopicPartition(record.topic(), record.partition()),
                                new OffsetAndMetadata(record.offset() + 1)
                        ));
                    } catch (Exception e) {
                        logger.error("Error processing bond price for key: {}, sending to DLQ", record.key());

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
