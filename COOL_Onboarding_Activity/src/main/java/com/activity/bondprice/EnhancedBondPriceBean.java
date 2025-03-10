package com.activity.bondprice;

import com.iontrading.talk.api.annotation.TalkType;


public class EnhancedBondPriceBean {
    public static int id=0;
    private final String className="EnhancedBondPriceBean";

    public String bondName;
    public String originalPrice;
    public String enhancedPrice;

    EnhancedBondPriceBean(String bondName, String originalPrice, String enhancedPrice) {
        this.bondName=bondName;
        this.enhancedPrice=enhancedPrice;
        this.originalPrice=originalPrice;
    }

    public String getClassName(){
        return className;
    }
}