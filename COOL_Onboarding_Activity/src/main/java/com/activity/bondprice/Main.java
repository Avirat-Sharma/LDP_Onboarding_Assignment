package com.activity.bondprice;
import com.iontrading.isf.applicationserver.spi.AS;
import com.iontrading.mkv.messagequeue.MkvMQ;

/**
 * Very simple message queue publisher: publishes and supplies on demand an
 * {@link MkvMQ}. The data pushed on the message queue represents Order
 * entities.
 *
 * Application start-up entry point.
 */
public final class Main {

    public static void main(String[] args) {
        AS.createLaunchConfiguration().withArgs(args).withModules(MkvMQPublisherModule.class).launch();
    }
}

