package com.activity.bondprice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iontrading.isf.commons.async.AsyncResult;
import com.iontrading.isf.commons.async.AsyncResultPromise;
import com.iontrading.isf.commons.async.AsyncResults;
import com.iontrading.isf.configuration.api.Configuration;
import com.iontrading.mkv.MkvSupply;
import com.iontrading.mkv.enums.MkvFieldType;
import com.iontrading.mkv.enums.MkvMQAction;
import com.iontrading.mkv.exceptions.MkvException;
import com.iontrading.mkv.messagequeue.MkvMQConf;
import com.iontrading.mkv_wrappers.IMkvMQ;
import com.iontrading.mkv_wrappers.IMkvMQManager;
import com.iontrading.mkv_wrappers.IMkvSupplyBuilder;
import com.iontrading.mkv_wrappers.IMkvType;
import com.iontrading.mkv_wrappers.factory.IMkvSupplyBuilderFactory;
import com.iontrading.mkv_wrappers.factory.IMkvTypeFactory;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MQFunction implements MQInterface {
    private IMkvMQ queue;
    private final IMkvMQManager mqManager;
    private IMkvType queueType;
    private final IMkvTypeFactory typeFactory;
    private final Configuration config;
    private final IMkvSupplyBuilderFactory supplyBuilderFactory;
    private Integer counter = 0;

    @Inject
    public MQFunction(IMkvMQManager mqManager, IMkvTypeFactory typeFactory, Configuration config, IMkvSupplyBuilderFactory supplyBuilderFactory) {
        this.mqManager = mqManager;
        this.typeFactory = typeFactory;
        this.config = config;
        this.supplyBuilderFactory = supplyBuilderFactory;
    }

    @Override
    public AsyncResult<String> openQueue() throws MkvException {
        this.queueType =  typeFactory.create(
                "PublishObject1", new String[] {"ID", "originalPrice", "enhancedPrice"}, new MkvFieldType[] {MkvFieldType.STR, MkvFieldType.STR, MkvFieldType.STR});
        queueType.publish();

        AsyncResultPromise<String> result = AsyncResults.create();
        try
        {
            String queueName = "CONSUMED.DATA" + counter++;
            var config = new MkvMQConf();
            this.queue = mqManager.create(queueName, queueType, config);
            result.success(queueName);
        }
        catch (MkvException e) {
            result.failure(e);
        }

        return result;
    }

    @Override
    public AsyncResult<Void> pushData() throws JsonProcessingException, ExecutionException, InterruptedException, MkvException {
        IMkvSupplyBuilder supplyBuilder = supplyBuilderFactory.create(queueType);
//        Properties consumerProps1 = PropertiesProv.getConsumerProperties();
//
//        Properties producerProps = PropertiesProv.getProducerProperties();
//
//        ConsumerListener listener = new MyConsumerListener();
//        Consumer consumer = new Consumer(consumerProps1, producerProps, listener);
//
//
//
//        consumer.start(queue, supplyBuilder);

        try{
            EnhancedBondPriceConsumer enhancedConsumer = new EnhancedBondPriceConsumer();
            ArrayList<EnhancedBondPriceBean> enhancedBonds = enhancedConsumer.getEnhancedBeans();
            for(EnhancedBondPriceBean enhancedBond:enhancedBonds){
                supplyBuilder.setField("ID",enhancedBond.bondName);
                supplyBuilder.setField("originalPrice",enhancedBond.originalPrice);
                supplyBuilder.setField("enhancedPrice",enhancedBond.enhancedPrice);

                MkvSupply supply = supplyBuilder.getSupply();
                queue.put(supply,"Bond Added: "+ enhancedBond.bondName, MkvMQAction.ADD);
                queue.flush();
            }
        }catch (Exception e) {
            System.err.println("error: " + e.getMessage());
        } finally {
            System.out.println("Closed Queue");
        }

        System.out.println(queue.getName());
        System.out.println(queue.getSize());

        System.out.println(queue.getSize());

        System.out.println(queue.toString());
//        queue.close(10);
        return AsyncResults.create();
    }
}
