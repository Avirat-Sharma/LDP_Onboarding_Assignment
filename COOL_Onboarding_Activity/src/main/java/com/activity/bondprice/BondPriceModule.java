package com.activity.bondprice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.iontrading.isf.boot.guice.BootModule;
import com.iontrading.isf.boot.spi.IBootService;
import com.iontrading.isf.boot.spi.IService;
import com.iontrading.isf.configuration.guice.ConfigurationModule;
import com.iontrading.isf.dependency_manager.providers.guice.ServiceManagerDependencyProviderModule;
import com.iontrading.isf.modules.annotation.ModuleDescriptor;
import com.iontrading.isf.service_manager.guice.ServiceManagerModule;
import com.iontrading.talk.api.guice.TalkModule;
import com.iontrading.talk.ionbus.guice.TalkIonBusModule;

@ModuleDescriptor(requires= {BootModule.class, ServiceManagerDependencyProviderModule.class, TalkIonBusModule.class})
public class BondPriceModule extends AbstractModule{
    @Override
    protected void configure(){
        //activating services
        ServiceManagerModule.addService(binder(),"InitProducer");
        ServiceManagerModule.addService(binder(),"Consumer");

        bind(TalkInterface.class).to(TalkFunction.class).in(Singleton.class);
    }

}