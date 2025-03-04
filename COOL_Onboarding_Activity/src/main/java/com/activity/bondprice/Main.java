package com.activity.bondprice;

public class Main {
    public static void main(String[] args) {
        // ✅ Initialize all components
        InitBondProducer initProd = new InitBondProducer();
        BondPriceProcessor processor = new BondPriceProcessor();
        BondPriceProducer producer = new BondPriceProducer();
        BondPriceConsumer consumer = new BondPriceConsumer(producer); // ✅ Pass producer

        // ✅ Produce initial bond prices
        initProd.produce();

        // ✅ Start consuming and processing
        consumer.consume(processor);
    }
}
