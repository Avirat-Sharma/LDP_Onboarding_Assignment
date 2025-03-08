package com.activity.bondprice;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class BondPriceConsumer {
    private static final Logger logger = LoggerFactory.getLogger(BondPriceConsumer.class);
    private final KafkaConsumer<String, String> consumer;
    private final BondPriceProducer producer; // Producer for publishing enhanced prices

    public BondPriceConsumer(BondPriceProducer producer) {
        Properties props = KafkaConfig.getConsumerProperties();
        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList("bond-prices"));
        this.producer = producer;
    }

    public void consume(BondPriceProcessor processor) {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        logger.info(" Received bond price -> Key: {}, Value: {}", record.key(), record.value());

                        //  Process bond price
                        String enhancedPrice = processor.enhanceBondPrice(record.key(), record.value());

                        //  Publish enhanced price
                        producer.send(record.key(), enhancedPrice);

                        //  Commit offset after successful processing
                        consumer.commitSync(Collections.singletonMap(
                                new TopicPartition(record.topic(), record.partition()),
                                new OffsetAndMetadata(record.offset() + 1)
                        ));
                    } catch (Exception e) {
                        logger.error("Error processing bond price for key: {}, sending to DLQ", record.key(), e);

                        // Send failed message to a Dead Letter Queue (DLQ) topic
                        producer.send(record.key(), record.value());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Consumer encountered an error", e);
        } finally {
            System.out.println("Consumer closed");
            return ;
        }
    }


}
