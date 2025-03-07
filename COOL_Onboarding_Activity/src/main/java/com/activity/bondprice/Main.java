package com.activity.bondprice;
import com.iontrading.isf.applicationserver.spi.AS;
public class Main {
    public static void main(String[] args) {
        // Initialize all components
//        InitBondProducer initProd = new InitBondProducer();
//        BondPriceProcessor processor = new BondPriceProcessor();
//        BondPriceProducer producer = new BondPriceProducer();
//        BondPriceConsumer consumer = new BondPriceConsumer(producer); //  Pass producer
//
//        //  Produce initial bond prices
//        initProd.produce();
//
//        // Start consuming and processing
//        consumer.consume(processor);
        AS.createLaunchConfiguration()
                .withArgs(args)
                .withModules(BondPriceModule.class)
                .withComponentInfo("BondPriceEnhancer", "Simple ION 2.0 Bond Price Application", "0.0.1", "na")
                .launch();
    }
}
