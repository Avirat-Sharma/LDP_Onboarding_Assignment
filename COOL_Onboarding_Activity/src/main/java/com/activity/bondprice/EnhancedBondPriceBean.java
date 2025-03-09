package com.activity.bondprice;

public class EnhancedBondPriceBean {
    public static int id=0;
    private final String className="EnhancedBondPriceBean";

    public String bondName;
    public String price;

    EnhancedBondPriceBean(String bondName, String price) {
        this.bondName=bondName;
        this.price=price;
    }

    public String getClassName(){
        return className;
    }
}