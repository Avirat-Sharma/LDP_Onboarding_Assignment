package com.activity.fcall;

import com.iontrading.isf.commons.async.AsyncResult;
import com.iontrading.talk.api.annotation.TalkFunction;
import com.iontrading.talk.api.annotation.TalkParam;
import com.iontrading.talk.ionbus.spi.IonBusInfo;

public class TalkType {
    private final String className="TalkType";

    @TalkProperty(name="EnhancedBond") public EnhancedBondPriceBean enhancedBond;
    @TalkProperty(name="OriginalBond") public BondPriceBean originalBond;

    public String getClassName() {
        return className;
    }
}