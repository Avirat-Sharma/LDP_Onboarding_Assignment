package com.activity.fcall;

import com.iontrading.talk.api.annotation.Identifier;
import com.iontrading.talk.api.annotation.TalkProperty;
import com.iontrading.talk.api.annotation.TalkType;

@TalkType(name="BondTalkType")
public class MyTalkType {
    private final String className="MyTalkType";

    @TalkProperty(name="EnhancedBond") public EnhancedBondPriceBean enhancedBond;
    @TalkProperty(name="OriginalBond") public BondPriceBean originalBond;

    MyTalkType(EnhancedBondPriceBean enhancedBond, BondPriceBean originalBond){
        this.enhancedBond=enhancedBond;
        this.originalBond=originalBond;
    }
    @TalkProperty(name="ClassName")
    public String getClassName() {
        return className;
    }
}