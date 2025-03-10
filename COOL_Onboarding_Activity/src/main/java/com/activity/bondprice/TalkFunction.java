package com.activity.bondprice;

import com.iontrading.isf.commons.async.AsyncResult;
import com.iontrading.isf.commons.async.AsyncResultPromise;
import com.iontrading.isf.commons.async.AsyncResults;
import com.iontrading.talk.api.Publisher;
import com.iontrading.talk.api.exception.TalkException;
import com.iontrading.talk.ionbus.spi.IonBusInfo;
import jakarta.inject.Inject;

public class TalkFunction implements TalkInterface{
    @Inject
    private Publisher publisher;
    @Override
    public AsyncResult<String> sendBonds(String bondName, String originalBondPrice , String enhancedBondPrice,IonBusInfo busInfo){
        AsyncResultPromise<String> result= AsyncResults.create();
        // will implement

        publisher.publish(new MyTalkType(bondName,originalBondPrice,enhancedBondPrice));
//        System.out.println("Bond ID Received: "+enhancedBond.bondName);
        if(result.isDone()){
            result.success("Success");
        }
        else{
            result.failure(new TalkException("Try again"));
        }
        return result;
    }
}