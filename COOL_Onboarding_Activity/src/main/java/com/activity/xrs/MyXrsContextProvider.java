package com.activity.xrs;

import com.iontrading.xrs.api.ContextInitInfoProvider;
import com.iontrading.xrs.api.lib.ContextInitInfo;
import com.iontrading.xrs.api.lib.ContextModuleCollection;

public class MyXrsContextProvider implements ContextInitInfoProvider {
    public ContextInitInfo getContext(){
        ContextModuleCollection moduleCollection = new ContextModuleCollection();
        moduleCollection.setStructureModule(new MyStructureModule());
        moduleCollection.setSnapshotModule(new MySnapshotModule());
        ContextInitInfo cinfo = new ContextInitInfo("KafkaApplication", "KafkaData", moduleCollection);
        cinfo.exportService("MyCOOLQueueService");
        cinfo.setSupportFromToDataSnapshot(true);
        cinfo.setSupportPagedQueues(true);
        return cinfo;

//        ContextInitInfo context = new ContextInitInfo();
//        context.setContextName("MyCOOLContext").setContextObjectName("MyCOOLContextObject");
//
//        ContextModuleCollection moduleCollection = new ContextModuleCollection();
//        moduleCollection.setStructureModule(new MyStructureModule());
//        moduleCollection.setSnapshotModule(new MySnapshotModule());
////        moduleCollection.setRealTimeModule(new MyRealtimeModule());
//        context.setModuleCollection(moduleCollection);
//        System.out.println("Context Created");
//        return context;
    }
}
