package com.activity.bondprice
import com.iontrading.isf.boot.spi.IService;
import com.iontrading.isf.service_manager.spi.IBusServiceManager;
import com.iontrading.isf.api.Publisher;
import jakarta.inject.Inject

public class BondPriceService implements IService{
    @Inject IBusServiceManager serviceManager;
    @Inject private Publisher publisher;

    public void start() throws Exception {
        serviceManager.addService("InitProducer");
        serviceManager.activateService("InitProducer")
        serviceManager.addService("Consumer");
        serviceManager.activatService("Consumer");

    }

    public void shutdown(){
        serviceManager.deactivateService("InitProducer");
        serviceManager.deactivateService("Consumer");
    }

}