package com.activity.fcall;
import com.iontrading.isf.commons.async.AsyncResult;
import com.iontrading.talk.api.annotation.TalkFunction;
import com.iontrading.talk.api.annotation.TalkParam;
import com.iontrading.talk.ionbus.spi.IonBusInfo;


public interface TalkInterface {
    @TalkFunction(name="SendBeansToPlatform")
    AsyncResult<String> sendBonds(
            @TalkParam(name="EnhancedBond") EnhancedBondPriceBean enhancedBond,
            @TalkParam(name="OriginalBond") BondPriceBean originalBond,
            IonBusInfo busInfo
    );
}