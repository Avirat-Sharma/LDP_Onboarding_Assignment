package com.activity.bondprice;

import com.iontrading.talk.api.annotation.Identifier;
import com.iontrading.talk.api.annotation.TalkProperty;
import com.iontrading.talk.api.annotation.TalkType;

@TalkType(name="BondTalkType")
public class MyTalkType {
    private final String className="MyTalkType";

//    @TalkProperty(name="EnhancedBond") public EnhancedBondPriceBean enhancedBond;
    @TalkProperty
    private String bondid;
    @TalkProperty
    private String enhancedPrice;
    @TalkProperty
    private String originalPrice;
//    @TalkProperty(name="OriginalBond") public BondPriceBean originalBond;

//    MyTalkType(EnhancedBondPriceBean enhancedBond){
//        this.enhancedBond=enhancedBond;
//        this.originalBond=originalBond;
//    }


    public MyTalkType(String bondid, String originalPrice, String enhancedPrice) {
        this.bondid = bondid;
        this.enhancedPrice = enhancedPrice;
        this.originalPrice = originalPrice;
    }

    @Identifier

    public String getBondid() {
        return bondid;
    }

    public void setBondid(String bondid) {
        this.bondid = bondid;
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