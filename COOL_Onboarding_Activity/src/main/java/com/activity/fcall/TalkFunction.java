package com.activity.fcall

import com.iontrading.isf.commons.async.AsyncResult;
import com.iontrading.isf.commons.async.AsyncResultPromise;
import com.iontrading.isf.commons.async.AsyncResults;
import com.iontrading.talk.api.exception.TalkException;
import com.iontrading.talk.ionbus.spi.IonBusInfo;

public class TalkFunction implements TalkInterface{
    @Override
    public AsyncResult<String> sendBonds(EnhancedBondPriceBean enhancedBond, BondPriceBean originalBond, IonBusInfo busInfo){
        AsyncResultPromise<String> result= AsyncResults.create();
        // will implement

        return result;
    }
}