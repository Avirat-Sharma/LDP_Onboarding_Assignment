package com.activity.fcall;

public class EnhancedBondPriceBean {
    public static int id=0;
    private final String className="EnhancedBondPriceBean";

    public String bondName;
    public String price;
    public String enhancedPrice;

    EnhancedBondPriceBean(String bondName, String price, String enhancedPrice) {
        this.bondName=bondName;
        this.price=price;
        this.enhancedPrice=enhancedPrice;
    }

    public String getClassName(){
        return className;
    }
}