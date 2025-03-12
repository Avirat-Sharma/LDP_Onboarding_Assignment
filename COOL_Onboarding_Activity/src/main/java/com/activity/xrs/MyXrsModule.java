package com.activity.xrs;

import com.google.inject.AbstractModule;
import com.iontrading.isf.modules.annotation.ModuleDescriptor;
import com.iontrading.xrs.guice.XrsModule;

@ModuleDescriptor(requires={XrsModule.class})
public class MyXrsModule extends AbstractModule {
    protected void configure(){
        System.out.println("Registering XRS Module");
    XrsModule.registerContext(binder(),MyXrsContextProvider.class);
    }
}
