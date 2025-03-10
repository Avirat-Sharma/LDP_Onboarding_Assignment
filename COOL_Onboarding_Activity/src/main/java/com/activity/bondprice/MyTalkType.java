package com.activity.bondprice;

import com.iontrading.talk.api.annotation.Identifier;
import com.iontrading.talk.api.annotation.TalkProperty;
import com.iontrading.talk.api.annotation.TalkType;

@TalkType(name="BondTalkType")
public class MyTalkType {
    private final String className="MyTalkType";

//    @TalkProperty(name="EnhancedBond") public EnhancedBondPriceBean enhancedBond;
    private String bondid;
    private String price;
//    @TalkProperty(name="OriginalBond") public BondPriceBean originalBond;

//    MyTalkType(EnhancedBondPriceBean enhancedBond){
//        this.enhancedBond=enhancedBond;
//        this.originalBond=originalBond;
//    }


    public MyTalkType(String bondid, String price) {
        this.bondid = bondid;
        this.price = price;
    }

    @Identifier
    public String getBondid() {
        return bondid;
    }

    public void setId(String bondid) {
        this.bondid = bondid;
    }

    public String getClassName() {
        return className;
    }

    @TalkProperty
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

//    @TalkProperty(name="ClassName")
//    public String getClassName() {
//        return className;
//    }
}