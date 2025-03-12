package com.activity.xrs;

import com.google.inject.AbstractModule;
import com.iontrading.isf.modules.annotation.ModuleDescriptor;
import com.iontrading.xrs.api.ContextInitInfoProvider;
import com.iontrading.xrs.api.lib.ContextInitInfo;
import com.iontrading.xrs.api.lib.ContextModuleCollection;
import com.iontrading.xrs.guice.XrsModule;

@ModuleDescriptor(requires={XrsModule.class})
public class MyXrsModule extends AbstractModule {
    protected void configure(){
        XrsModule.registerContext(binder(), new ContextInitInfoProvider() {

            public ContextInitInfo getContext() {
                /*
                 * Initialize a single context, called "Assignment". The
                 * managed object is "People" so that the search function will
                 * be named "SearchPeople".
                 *
                 * Register only the mandatory modules, Structure and Snapshot,
                 * plus the RealtimeModule to deal with live data.
                 */
                ContextModuleCollection moduleCollection = new ContextModuleCollection();
                moduleCollection.setStructureModule(new MyStructureModule());
                moduleCollection.setSnapshotModule(new MySnapshotModule());
                ContextInitInfo cinfo = new ContextInitInfo("Assignment", "PublisherObjectContext", moduleCollection);
                cinfo.exportService("PUBLISHEROBJECTDATA");
                cinfo.setSupportFromToDataSnapshot(true);
                cinfo.setSupportPagedQueues(true);
                return cinfo;
            }
        });

    }
}
