package com.activity.bondprice;

//import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.iontrading.isf.boot.guice.BootModule;
import com.iontrading.isf.boot.spi.IBootService.RunPhase;
import com.iontrading.isf.modules.annotation.ModuleDescriptor;
import com.iontrading.isf.service_manager.guice.ServiceManagerModule;
import com.iontrading.talk.api.guice.TalkModule;
import com.iontrading.talk.ionbus.guice.TalkIonBusModule;

/**
 * Guice module. Publishes the component bus functions and the platform service.
 */
@ModuleDescriptor(requires = { BootModule.class, ServiceManagerModule.class, TalkIonBusModule.class })
public class MkvMQPublisherModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MkvMQPublisherService.class).in(Singleton.class);
        bind(PublisherBusFunctions.class).to(MkvMQPublisherService.class);

        BootModule.registerBootService(binder(), MkvMQPublisherService.class, RunPhase.START);
        TalkModule.exportFunctions(binder(), PublisherBusFunctions.class);
        ServiceManagerModule.addService(binder(), "MQPublisherService");
    }

}
