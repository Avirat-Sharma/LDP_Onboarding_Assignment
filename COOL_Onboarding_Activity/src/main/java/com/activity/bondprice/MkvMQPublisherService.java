package com.activity.bondprice;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


import com.google.inject.Inject;
import com.iontrading.isf.boot.spi.IService;
import com.iontrading.isf.commons.async.AsyncResult;
import com.iontrading.isf.commons.async.AsyncResultPromise;
import com.iontrading.isf.commons.async.AsyncResults;
import com.iontrading.isf.configuration.api.Configuration;
import com.iontrading.isf.service_manager.spi.IBusServiceManager;
import com.iontrading.mkv.MkvSupply;
import com.iontrading.mkv.enums.MkvFieldType;
import com.iontrading.mkv.enums.MkvMQAction;
import com.iontrading.mkv.exceptions.MkvException;
import com.iontrading.mkv.messagequeue.MkvMQ;
import com.iontrading.mkv.messagequeue.MkvMQConf;
import com.iontrading.mkv_wrappers.IMkvMQ;
import com.iontrading.mkv_wrappers.IMkvMQManager;
import com.iontrading.mkv_wrappers.IMkvSupplyBuilder;
import com.iontrading.mkv_wrappers.IMkvType;
import com.iontrading.mkv_wrappers.factory.IMkvSupplyBuilderFactory;
import com.iontrading.mkv_wrappers.factory.IMkvTypeFactory;

/**
 * Provides all the publisher component platform services. It is responsible of:
 *
 * - opening message queues on demand
 *
 * - pushing data and events on the message queues
 */
public class MkvMQPublisherService implements IService, PublisherBusFunctions {

    private IBusServiceManager serviceManager;
    private Set<IMkvMQ> queues;
    private IMkvMQManager mqManager;
    private AtomicInteger counter;
    private IMkvType queueType;
    private IMkvTypeFactory typeFactory;
    private Configuration config;
    private IMkvSupplyBuilderFactory supplyBuilderFactory;

    @Inject
    public MkvMQPublisherService(IBusServiceManager serviceManager, IMkvMQManager mqManager,
                                 IMkvTypeFactory typeFactory, IMkvSupplyBuilderFactory supplyBuilderFactory, Configuration config) {
        this.serviceManager = serviceManager;
        this.mqManager = mqManager;
        this.typeFactory = typeFactory;
        this.supplyBuilderFactory = supplyBuilderFactory;
        this.config = config;
        this.queues = new HashSet<IMkvMQ>();
        this.counter = new AtomicInteger(0);
    }

    /**
     * Implementation of the OpenOrderQueue bus function.
     * <p>
     * It publishes an {@link MkvMQ} and reply to the caller with the queue
     * name.
     * <p>
     * This function is called by the subscriber sample component.
     */
    @Override
    public AsyncResult<String> openOrderQueue() {
        AsyncResultPromise<String> result = AsyncResults.create();
        try {
            /**
             * Create and publish the queue
             */
            String queueName = "ANY.PRICE." + config.get("SOURCE") + "." + getQueueId();
            IMkvMQ queue = mqManager.create(queueName, queueType, new MkvMQConf());
            queues.add(queue);

            /**
             * Supply a default snapshot of Orders
             */
            queue.put(MkvMQAction.BATCHSTART);
            pushDefaultOrdersSnapshot(queue);


            /**
             * Reply to the function call
             */
            result.success(queueName);
        } catch (MkvException e) {
            result.failure(e);
        }
        return result;
    }

    private String getQueueId() {
        return "Price" + counter.getAndIncrement();
    }

    @Override
    public String getName() {
        return "MQPublisherService";
    }

    @Override
    public void shutdown() {
        serviceManager.activateService(getName());
    }

    @Override
    public void start() throws Exception {
        serviceManager.deactivateService(getName());
        initType();
    }

    /**
     * Implementation of the PushAddOrderEvent bus function.
     * <p>
     * It pushes a new order on all the opened message queues.
     */
    @Override
    public void pushAddOrderEvent(String id, String currencyPair, String verb, double price, double qty, String trader)
            throws MkvException {
        for (IMkvMQ q : queues) {
            pushDataEvent(q, MkvMQAction.ADD, id, currencyPair, verb, price, qty, trader);
        }
    }

    /**
     * Implementation of the PushUpdOrderEvent bus function.
     * <p>
     * It pushes an order update on all the opened message queues.
     */
    @Override
    public void pushUpdOrderEvent(String id, String currencyPair, String verb, double price, double qty, String trader)
            throws MkvException {
        for (IMkvMQ q : queues) {
            pushDataEvent(q, MkvMQAction.REWRITE, id, currencyPair, verb, price, qty, trader);
        }
    }

    /**
     * Implementation of the PushDelOrderEvent bus function.
     * <p>
     * It pushes a delete order event on all the opened message queues.
     */
    @Override
    public void pushDelOrderEvent(String id, String currencyPair, String verb, double price, double qty, String trader)
            throws MkvException {
        for (IMkvMQ q : queues) {
            pushDataEvent(q, MkvMQAction.DELETE, id, currencyPair, verb, price, qty, trader);
        }
    }

    /**
     * Implementation of the PushUserAction bus function.
     * <p>
     * It pushes a user action on all the opened message queues.
     */
    @Override
    public void pushUserAction(int code, String msg) throws MkvException {
        for (IMkvMQ q : queues) {
            q.putUserAction(code + ":" + msg);
        }
    }

    /**
     * Defines the type of the publisher message queues.
     */
    private void initType() throws MkvException {
        this.queueType = typeFactory.create("ORDER",
                new String[]{"Id", "CurrencyPair", "Verb", "Price", "Qty", "Trader"},
                new MkvFieldType[]{MkvFieldType.STR, MkvFieldType.STR, MkvFieldType.STR, MkvFieldType.REAL,
                        MkvFieldType.REAL, MkvFieldType.STR});
        queueType.publish();
    }

    private void pushDefaultOrdersSnapshot(IMkvMQ queue) throws MkvException {
        pushDataEvent(queue, MkvMQAction.ADD, "O0", "EUR/USD", "Buy", 99.0, 500.0, "Trader A");
        pushDataEvent(queue, MkvMQAction.ADD, "O1", "EUR/USD", "Buy", 97.0, 1500.0, "Trader B");
        pushDataEvent(queue, MkvMQAction.ADD, "O2", "EUR/USD", "Sell", 101.2, 200.0, "Trader A");
        pushDataEvent(queue, MkvMQAction.ADD, "O3", "EUR/USD", "Sell", 100.3, 400.0, "Trader C");
        pushDataEvent(queue, MkvMQAction.ADD, "O4", "EUR/USD", "Sell", 99.6, 520.0, "Trader B");
        pushDataEvent(queue, MkvMQAction.ADD, "O5", "EUR/USD", "Buy", 98.2, 495.0, "Trader A");
    }

    private void pushDataEvent(IMkvMQ queue, MkvMQAction action, String id, String currencyPair, String verb,
                               double price, double qty, String trader) throws MkvException {
        IMkvSupplyBuilder supplyBuilder = supplyBuilderFactory.create(queueType);
        supplyBuilder.setField("Id", id);
        supplyBuilder.setField("CurrencyPair", currencyPair);
        supplyBuilder.setField("Verb", verb);
        supplyBuilder.setField("Price", price);
        supplyBuilder.setField("Qty", qty);
        supplyBuilder.setField("Trader", trader);
        MkvSupply supply = supplyBuilder.getSupply();
        queue.put(supply, id, action);
        queue.put(MkvMQAction.BATCHEND);
        queue.flush();
    }
}

