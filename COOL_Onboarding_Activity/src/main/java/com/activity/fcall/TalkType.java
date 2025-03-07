package com.activity.fcall;

import com.iontrading.talk.api.annotation.Identifier;
import com.iontrading.talk.api.annotation.TalkProperty;
import com.iontrading.talk.api.annotation.TalkType;

@TalkType(name="BondTalkType")
public class TalkType {
    private final String className="TalkType";

    @TalkProperty(name="EnhancedBond") public EnhancedBondPriceBean enhancedBond;
    @TalkProperty(name="OriginalBond") public BondPriceBean originalBond;

    TalkType(EnhancedBondPriceBean enhancedBond, BondPriceBean originalBond){
        this.enhancedBond=enhancedBond;
        this.originalBond=originalBond;
    }
    @TalkProperty(name="ClassName")
    public String getClassName() {
        return className;
    }
}