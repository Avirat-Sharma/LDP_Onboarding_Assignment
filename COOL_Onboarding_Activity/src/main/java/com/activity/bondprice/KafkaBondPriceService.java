package com.activity.bondprice;

import com.iontrading.isf.boot.spi.IService;
import com.iontrading.isf.service_manager.spi.IBusServiceManager;
import com.iontrading.talk.api.Publisher;
import jakarta.inject.Inject;
import com.iontrading.talk.functions.spi.Importer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

public class KafkaBondPriceService implements IService {
    @Inject
    IBusServiceManager serviceManager;
    @Inject
    private Publisher publisher;
    @Inject
    private Importer importer;
    private TalkInterface remoteFunction;
    private static final Logger logger = LoggerFactory.getLogger(KafkaBondPriceService.class);

    public String getName() {
        return "KafkaService";
    }

    public void start() throws Exception {
        this.remoteFunction = importer.importFunctions(TalkInterface.class);

        serviceManager.addService("KafkaService", "", "");
        serviceManager.activateService("KafkaService");

        // Initialize all components
//        InitBondProducer initProd = new InitBondProducer();
//        BondPriceProcessor processor = new BondPriceProcessor();
//        BondPriceProducer producer = new BondPriceProducer();
//        BondPriceConsumer consumer = new BondPriceConsumer(producer); // Pass producer
//
//        // Produce initial bond prices
//        initProd.produce();
//
//        // Start consuming and processing
//        try {
//            while (true) {
//                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
//
//                for (ConsumerRecord<String, String> record : records) {
//                    try {
//                        logger.info("Received bond price -> Key: {}, Value: {}", record.key(), record.value());
//                        BondPriceBean originalBond = new BondPriceBean(record.key(), record.value());
//
//                        // Process bond price
//                        String enhancedPrice = processor.enhanceBondPrice(record.key(), record.value());
//                        EnhancedBondPriceBean enhancedBond = new EnhancedBondPriceBean(record.key(), originalBond.price, enhancedPrice);
//
//                        // Publish enhanced price
//                        producer.send(record.key(), enhancedPrice);
//
//                        // Commit offset after successful processing
//                        consumer.commitSync(Collections.singletonMap(
//                                new TopicPartition(record.topic(), record.partition()),
//                                new OffsetAndMetadata(record.offset() + 1)
//                        ));
//                        publisher.publish(new MyTalkType(enhancedBond, originalBond));
//
//                    } catch (Exception e) {
//                        logger.error("Error processing bond price for key: {}, sending to DLQ", record.key(), e);
//
//                        // Send failed message to a Dead Letter Queue (DLQ) topic
//                        producer.send(record.key(), record.value());
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            logger.error("Consumer encountered an error", e);
//
//        } finally {
//            consumer.close();
//        }
        BondPriceBean originalBond = new BondPriceBean("US101","100");
        EnhancedBondPriceBean enhancedBond = new EnhancedBondPriceBean("US101","100","102");
        publisher.publish(new MyTalkType(enhancedBond,originalBond));
    }

    public void shutdown() {
        serviceManager.deactivateService("KafkaService");
    }
}