package com.activity.bondprice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iontrading.isf.boot.spi.IService;
import com.iontrading.isf.commons.callback.Callback;
import com.iontrading.isf.commons.callback.ExceptionProofCallback;
import com.iontrading.isf.service_manager.spi.IBusServiceManager;
import com.iontrading.talk.api.Publisher;
import com.iontrading.talk.functions.spi.Exporter;
import com.iontrading.talk.ionbus.spi.IonBusInfo;
import jakarta.inject.Inject;
import com.iontrading.talk.functions.spi.Importer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;

public class KafkaBondPriceService implements IService {
    @Inject
    IBusServiceManager serviceManager;
    @Inject
    private TalkInterface remoteFunction;
    @Inject
    private MQInterface
            remoteMQFunction;

    private static final Logger logger = LoggerFactory.getLogger(KafkaBondPriceService.class);

    public String getName() {
        return "KafkaService";
    }

    public void start() throws Exception {
        // Import the remote TalkFunction implementation

//        serviceManager.addService("KafkaService", "", "");
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
        // consume and send enhanced bond
        consumer.consume(processor);
        // Get the enhanced bonds to be sent
        ArrayList<EnhancedBondPriceBean> enhancedBonds = enhancedConsumer.getEnhancedBeans();

        System.out.println("Publishing Data To ION Bus");

        // Iterate over each enhanced bond and send using TalkFunction
//        for (EnhancedBondPriceBean enhancedBond : enhancedBonds) {
//            IonBusInfo busInfo = new IonBusInfo();  // Ensure this is properly initialized
//            remoteFunction.sendBonds(enhancedBond.bondName, enhancedBond.originalPrice, enhancedBond.enhancedPrice, busInfo)
//                    .addCallback(new Callback<String>() {
//                        @Override
//                        public void onSuccess(String result) {
//                            logger.info("Successfully sent bond data: " + result);
//                        }
//
//                        @Override
//                        public void onFailure(Throwable exception) {
//                            logger.error("Failed to send bond data", exception);
//                            recoveryMeasure(exception);
//                        }
//                    });
//        }
        System.out.println("Success In Publishing Data");

        System.out.println("Publishing data using queue");

//        MQFunction remoteMQFunction = new MQFunction(new)
//        remoteMQFunction.openQueue()
//                .addCallback(new ExceptionProofCallback<String>() {
//                    @Override
//                    public void onTrySuccess(String result) {
//                        logger.info("Successfully open queue: " + result);
//                        try {
//                            remoteMQFunction.pushData().addCallback(new ExceptionProofCallback<Void>() {
//                                @Override
//                                public void onTrySuccess(Void unused) {
//                                    System.out.println("hello");
//                                }
//
//                                @Override
//                                public void onFailure(Throwable throwable) {
//                                    System.out.println("failure");
//                                }
//                            });
//                        } catch (Exception e) {
//                            System.out.println("failure");
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable exception) {
//                        logger.error("Failed opening queue", exception);
//                        recoveryMeasure(exception);
//                    }
//                });

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
