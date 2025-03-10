package com.activity.bondprice;

import com.iontrading.talk.api.annotation.Identifier;
import com.iontrading.talk.api.annotation.TalkProperty;
import com.iontrading.talk.api.annotation.TalkType;

@TalkType(name="BondTalkType")
public class MyTalkType {
    private final String className="MyTalkType";

//    @TalkProperty(name="EnhancedBond") public EnhancedBondPriceBean enhancedBond;
    @TalkProperty
    private String bondId;
    @TalkProperty
    private String enhancedPrice;
    @TalkProperty
    private String originalPrice;
//    @TalkProperty(name="OriginalBond") public BondPriceBean originalBond;

//    MyTalkType(EnhancedBondPriceBean enhancedBond){
//        this.enhancedBond=enhancedBond;
//        this.originalBond=originalBond;
//    }


    public MyTalkType(String bondId, String originalPrice, String enhancedPrice) {
        this.bondId = bondId;
        this.enhancedPrice = enhancedPrice;
        this.originalPrice = originalPrice;
    }

    @Identifier

    public String getBondId() {
        return bondId;
    }

    public void setBondId(String bondId) {
        this.bondId = bondId;
    }

    public String getEnhancedPrice() {
        return enhancedPrice;
    }

    public void setEnhancedPrice(String enhancedPrice) {
        this.enhancedPrice = enhancedPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }


    public String getClassName() {
        return className;
    }



//    @TalkProperty(name="ClassName")
//    public String getClassName() {
//        return className;
//    }
}