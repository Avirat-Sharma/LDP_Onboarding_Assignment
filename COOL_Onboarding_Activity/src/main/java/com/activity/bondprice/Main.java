package com.activity.bondprice;

public class Main {
    public static void main(String[] args) {
        BondPriceProcessor processor = new BondPriceProcessor();
        BondPriceProducer producer = new BondPriceProducer();
        BondPriceConsumer consumer = new BondPriceConsumer();

        consumer.consume(processor, producer);
    }
}
