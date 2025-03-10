package com.activity.bondprice;

import com.iontrading.isf.commons.async.AsyncResult;
import com.iontrading.talk.api.annotation.TalkFunction;
import com.iontrading.talk.api.annotation.TalkParam;
import com.iontrading.talk.ionbus.spi.IonBusInfo;


public interface TalkInterface {
    @TalkFunction(name="SendBeanToPlatform")
    AsyncResult<String> sendBonds(
            @TalkParam(name="BondName") String bondName,
            @TalkParam(name="OriginalBondPrice") String originalBondPrice,
            @TalkParam(name="EnhancedBondPrice") String enhancedBondPrice,
            IonBusInfo busInfo
    );
}