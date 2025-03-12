package com.activity.xrs;

import com.iontrading.xrs.api.*;
import com.iontrading.xrs.api.events.IEventFactory;
import com.iontrading.xrs.api.events.IEventMgr;
import com.iontrading.xrs.api.events.XRSEventException;
import com.iontrading.xrs.api.helper.SnapshotModuleHelper;
import com.iontrading.xrs.api.helper.SnapshotModuleHelperImpl;

public class MySnapshotModule implements ISnapshotModule {
    private IModuleServiceLocator locator;
    private IContext context;
    private volatile SnapshotModuleHelper helper;
    private ModuleStatus status = new ModuleStatus(XRSStatus.DISCONNECTED, "Starting up");

    @Override public void init(IContext context, IModuleServiceLocator locator) {
        this.context = context;
        this.locator = locator;
        status = new ModuleStatus(XRSStatus.RUNNING, "Connected!");
        this.helper = SnapshotModuleHelperImpl.create(locator);
    }

    @Override
    public void shutDown() {
        helper.shutDown();
    }

    @Override
    public String getName() {
        return "MySnapshotModule";
    }

    @Override
    public ModuleStatus getModuleStatus() {
        return null;
    }

    @Override
    public String getDetails() {
        return "";
    }

    @Override public XRSGenericResult prepareQuery(IQuery query) {
        return XRSGenericResult.ok();
    }
    @Override public void onQueryReadyForSnapshot(final IQuery query) {

        helper.onQueryReadyForSnapshot(query);

        IEventMgr evtMgr = locator.getEvtManager();
        IEventFactory evtFct = locator.getEventFactory();
        try {
            evtMgr.pushEvent(evtFct.createSnapshotStartEvent(context, query));
            MyXRSObject o = new MyXRSObject("Avirat", context);
            o.setFieldValue("Id", "MYID");
            evtMgr.pushEvent(evtFct.createSnapshotObjectEvent(context,query, o));
            evtMgr.pushEvent(evtFct.createSnapshotEndEvent(context, query));
            System.out.println("Event Pushed");
        } catch (XRSEventException e) {
            throw new RuntimeException(e);
        }

//        try{
//            evtMgr.pushEvent(evtFct.createSnapshotObjectEvent(context,query,new MyXRSObject("Avirat")));
//            System.out.println("Event Pushed");
//        } catch(XRSEventException e){
//            throw new RuntimeException(e);
//        }
//        try {
//            evtMgr.pushEvent(evtFct.createSnapshotEndEvent(context, query));
//        } catch (XRSEventException e) {
//            throw new RuntimeException(e);
//        }
    }
    @Override public void closeQuery(IQuery query) {
// Done with the query (correct completion or abort). Free resources
        helper.closeQuery(query);
    }
    @Override public void pauseQuery(IQuery query) {
// Suspend the query due e.g. client congestion
        helper.pauseQuery(query);
    }
    @Override public void resumeQuery(IQuery query) {
// Restart a suspended query
        helper.resumeQuery(query);
    }
}

