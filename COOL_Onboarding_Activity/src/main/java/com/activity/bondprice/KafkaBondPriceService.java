package com.activity.bondprice;
import com.iontrading.isf.boot.spi.IService;
import com.iontrading.isf.service_manager.spi.IBusServiceManager;
import com.iontrading.isf.api.Publisher;
import jakarta.inject.Inject;
import com.activity.fcall.BondPriceBean;
import com.activity.fcall.EnhancedBondPriceBean ;
import com.activity.fcall.TalkType;
public class BondPriceService implements IService{
    @Inject IBusServiceManager serviceManager;
    @Inject private Publisher publisher;

    public String getName(){
        return "KafkaService";
    }
    public void start() throws Exception {

        serviceManager.addService("KafkaService");
        serviceManager.activatService("KafkaService");
        //Initialize all components
        InitBondProducer initProd = new InitBondProducer();
        BondPriceProcessor processor = new BondPriceProcessor();
        BondPriceProducer producer = new BondPriceProducer();
        BondPriceConsumer consumer = new BondPriceConsumer(producer); //  Pass producer

        //  Produce initial bond prices
        initProd.produce();

        // Start consuming and processing
//        consumer.consume(processor);
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        logger.info(" Received bond price -> Key: {}, Value: {}", record.key(), record.value());
                        BondPriceBean originalBond = new BondPriceBean(record.key(),record.value());

                        //  Process bond price
                        String enhancedPrice = processor.enhanceBondPrice(record.key(), record.value());
                        EnhancedBondPriceBean enhancedBond = new EnhancedBondPriceBean(record.key(),originalBond.price,enhancedPrice);
                        //  Publish enhanced price
                        producer.send(record.key(), enhancedPrice);

                        //  Commit offset after successful processing
                        consumer.commitSync(Collections.singletonMap(
                                new TopicPartition(record.topic(), record.partition()),
                                new OffsetAndMetadata(record.offset() + 1)
                        ));
                        publisher.publish(new TalkType(enhancedBond,originalBond));

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
            consumer.close();
        }
    }
    }

    public void shutdown(){
        serviceManager.deactivateService("KafkaService");
    }

}