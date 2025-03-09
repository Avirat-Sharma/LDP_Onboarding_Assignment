package com.activity.bondprice;

import com.iontrading.isf.boot.spi.IService;
import com.iontrading.isf.commons.callback.Callback;
import com.iontrading.isf.service_manager.spi.IBusServiceManager;
import com.iontrading.talk.api.Publisher;
import com.iontrading.talk.ionbus.spi.IonBusInfo;
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
import java.util.ArrayList;
import java.util.Collections;

public class KafkaBondPriceService implements IService {
    @Inject
    IBusServiceManager serviceManager;
    @Inject
    private Importer importer;
    private TalkInterface remoteFunction;
    private static final Logger logger = LoggerFactory.getLogger(KafkaBondPriceService.class);

    public String getName() {
        return "KafkaService";
    }

    public void start() throws Exception {
        // Import the remote TalkFunction implementation
        this.remoteFunction = importer.importFunctions(TalkInterface.class);

        serviceManager.addService("KafkaService", "", "");
        serviceManager.activateService("KafkaService");
        System.out.println("Individual Kafka Service started");

        // Initialize components
        InitBondProducer initProd = new InitBondProducer();
        BondPriceProcessor processor = new BondPriceProcessor();
        BondPriceProducer producer = new BondPriceProducer();
        BondPriceConsumer consumer = new BondPriceConsumer(producer);
        EnhancedBondPriceConsumer enhancedConsumer = new EnhancedBondPriceConsumer();

        // Produce initial bond prices
        initProd.produce();
        consumer.consume(processor);

        // Get the enhanced bonds to be sent
        ArrayList<EnhancedBondPriceBean> enhancedBonds = enhancedConsumer.getEnhancedBeans();

        System.out.println("Publishing Data To ION Bus");

        // Iterate over each enhanced bond and send using TalkFunction
        for (EnhancedBondPriceBean enhancedBond : enhancedBonds) {
            IonBusInfo busInfo = new IonBusInfo();  // Ensure this is properly initialized
            remoteFunction.sendBonds(enhancedBond, busInfo)
                    .addCallback(new Callback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            logger.info("Successfully sent bond data: " + result);
                        }

                        @Override
                        public void onFailure(Throwable exception) {
                            logger.error("Failed to send bond data", exception);
                            recoveryMeasure(exception);
                        }
                    });
        }
        System.out.println("Success In Publishing Data");
    }

    public void shutdown() {
        serviceManager.deactivateService("KafkaService");
        System.out.println("Stopping Kafka Service");
    }

    private void recoveryMeasure(Throwable exception) {
        logger.warn("Recovery action triggered due to failure: " + exception.getMessage());
        // Implement retry logic or error handling as required
    }
}
