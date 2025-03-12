package com.activity.xrs;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;


import com.iontrading.xrs.api.IContext;
import com.iontrading.xrs.api.IModuleServiceLocator;
import com.iontrading.xrs.api.IQuery;
import com.iontrading.xrs.api.ISnapshotModule;
import com.iontrading.xrs.api.ModuleStatus;
import com.iontrading.xrs.api.XRSGenericResult;
import com.iontrading.xrs.api.XRSStatus;
import com.iontrading.xrs.api.events.IEventFactory;
import com.iontrading.xrs.api.events.IEventMgr;
import com.iontrading.xrs.api.events.XRSEventException;
import com.iontrading.xrs.api.helper.SnapshotModuleHelper;
import com.iontrading.xrs.api.helper.SnapshotModuleHelperImpl;

/**
 * Not surprisingly, the SnapshotModule is responsible for the retrieval of the
 * snapshot data.
 * <p>
 * Apart from those methods that are common to all the modules, this module acts
 * as a listener that is invoked by the xRS in correspondence of each phase of
 * the following query life-cycle:
 * <ul>
 * <li>Validation: {@link #prepareQuery(IQuery)} is invoked at the end of the
 * validation phase. The module takes the necessary actions to handle the
 * snapshot retrieval for given query and to decide whether the query should be
 * discarded or not.
 * <li>Start: {@link #onQueryReadyForSnapshot(IQuery)} is invoked as soon as the
 * module is allowed to start pushing data for the query. When this method is
 * invoked, the underlying queue (if any) is already open and subscribed by the
 * client.
 * <li>Pause/Resume: {@link #pauseQuery(IQuery)} and
 * {@link #resumeQuery(IQuery)} are called by the xRS to notify the module when
 * the data is filling the internal xRS buffers too quickly (e.g. the client is
 * consuming the data too slowly). The module should pause (or throttle) and
 * respectively resume the number of objects it produces.
 * <li>Close: When the {@link #closeQuery(IQuery)} is invoked, the corresponding
 * request no longer has to be served by the xRS.
 * </ul>
 * <p>
 */
public class MySnapshotModule implements ISnapshotModule {
    private volatile IModuleServiceLocator locator;
    private volatile IContext context;
    private volatile SnapshotModuleHelper helper;

    private volatile ModuleStatus status = new ModuleStatus(XRSStatus.DISCONNECTED, "Disconnected");

//    private Consumer consumer;
    private ArrayList<ArrayList<String>> dataConsumed;

    @Override
    public void init(IContext context, IModuleServiceLocator locator) {
        /*
         * Save the context and the service-locator. Both these objects are
         * necessary to push events towards the xRS.
         */
        this.context = context;
        this.locator = locator;
        this.helper = SnapshotModuleHelperImpl.create(locator);

        dataConsumed = new ArrayList<>();

        ArrayList<String> curr = new ArrayList<>();
        curr.add("key1");
        curr.add("value1");

        dataConsumed.add(curr);
        /*
         * The sample module uses the following object as an abstraction of a
         * physical database.
         */
//        this.db = Database.getDatabase();
//        db.init(context, locator.getConfig().getXRSSource());
        /*
         * All the required resources are available, so the module status is
         * always RUNNING in this (unrealistic) case.
         */
        this.status = new ModuleStatus(XRSStatus.RUNNING, "Running");
//        Properties consumerProps1 = PropertiesProv.getConsumerProperties();
//
//        Properties producerProps = PropertiesProv.getProducerProperties();
//
//        ConsumerListener listener = new MyConsumerListener();
//        this.consumer = new Consumer(consumerProps1, producerProps, listener);
//
//        this.dataConsumed = consumer.start();
    }

    @Override
    public String getName() {
        return "MySnapshot";
    }

    @Override
    public String getDetails() {
        return "...";
    }

    @Override
    public XRSGenericResult prepareQuery(IQuery query) {
        /*
         * In this simple example, we do nothing to set up the query.
         *
         * A real module must prepare the required data structures, generate the
         * SQL code, run some query on the database or retrieve some platform
         * data... basically do whatever is required to be ready for the data
         * retrieval. Note that, at this stage, the query is not enabled yet to
         * accept data.
         *
         * Return XRSGenericResult.ok() if the module is able to deal with given
         * query, or XRSGenericResult.error() otherwise. It is worth noting that
         * the query syntax and semantics have been already validated, so an
         * error at this stage is mainly due to "technical reasons".
         */
        return XRSGenericResult.ok();
    }

    @Override
    public void onQueryReadyForSnapshot(final IQuery query) {
        /*
         * From now on the module is allowed to push data for given query. The
         * xRS uses a dedicated thread to call this method and it doesn't impose
         * that the events have to be pushed by this same thread. Thus,
         * basically, the implementation is up to the developer... you can do
         * whatever you want.
         */

        helper.onQueryReadyForSnapshot(query);

        /*
         * The evtMgr and evtFct are main access points that each module can use
         * for pushing events towards the xRS.
         */
        final IEventMgr evtMgr = locator.getEvtManager();
        final IEventFactory evtFct = locator.getEventFactory();
        try {
            // by default does not provide a page
            AtomicInteger counter = new AtomicInteger(0);
//            for(ArrayList<String> data : dataConsumed) {
//                System.out.println();
//            }

            for(ArrayList<String> data : dataConsumed) {
                MyObject o = new MyObject(counter.incrementAndGet() + "_id", context);
                o.setFieldValue("Key", data.get(0));
                o.setFieldValue("Value", data.get(1));

                evtMgr.pushEvent(evtFct.createSnapshotObjectEvent(context, query, o));
            }
//            MyObject o = new MyObject(counter.get() + "_id", context);
//            o.setFieldValue("Key", "myKey");
//            o.setFieldValue("Value", "myvalue");
//
//            evtMgr.pushEvent(evtFct.createSnapshotObjectEvent(context, query, o));
//            evtMgr.pushEvent(evtFct.createSnapshotEndEvent(context, query));

            /*
             * The sample module pushes _the whole database_ in reply of every
             * query, despite of the user request (i.e. the filtering criteria).
             *
             * Even though this is not a wise approach - mainly because of
             * performance - the xRS framework guarantees that the pushed data
             * will be filtered and only the records that match the filtering
             * criteria will be actually delivered to the client.
             *
             * The SnapshotModule is expected to push _at least_ all the records
             * that match the user query.
             */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeQuery(IQuery query) {
        helper.closeQuery(query);
        /*
         * The sample module doesn't need to act on this.
         *
         * In real-life it is the notification that a query has been aborted,
         * either by the client or the xRS, and the module must free the related
         * resources
         */
    }

    @Override
    public ModuleStatus getModuleStatus() {
        return status;
    }

    @Override
    public void shutDown() {
        helper.shutDown();
    }

    @Override
    public void pauseQuery(IQuery query) {
        helper.pauseQuery(query);
    }

    @Override
    public void resumeQuery(IQuery query) {
        helper.resumeQuery(query);
    }
}
