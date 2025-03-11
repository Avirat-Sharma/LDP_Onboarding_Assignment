package com.activity.bondprice;
import com.iontrading.isf.commons.async.AsyncResult;
import com.iontrading.mkv.exceptions.MkvException;
import com.iontrading.talk.api.annotation.TalkFunction;
import com.iontrading.talk.api.annotation.TalkParam;
import com.iontrading.talk.api.annotation.TalkResult;

/**
 * Publisher component bus functions.
 */
public interface PublisherBusFunctions {

    @TalkFunction(result = @TalkResult(name = "QueueName", overview = true) )
    AsyncResult<String> openOrderQueue();

    @TalkFunction
    void pushAddOrderEvent(@TalkParam(name = "Id") String id, @TalkParam(name = "CurrencyPair") String currencyPair,
                           @TalkParam(name = "Verb") String verb, @TalkParam(name = "Price") double price,
                           @TalkParam(name = "Qty") double qty, @TalkParam(name = "Trader") String trader) throws MkvException;

    @TalkFunction
    void pushUpdOrderEvent(@TalkParam(name = "Id") String id, @TalkParam(name = "CurrencyPair") String currencyPair,
                           @TalkParam(name = "Verb") String verb, @TalkParam(name = "Price") double price,
                           @TalkParam(name = "Qty") double qty, @TalkParam(name = "Trader") String trader) throws MkvException;

    @TalkFunction
    void pushDelOrderEvent(@TalkParam(name = "Id") String id, @TalkParam(name = "CurrencyPair") String currencyPair,
                           @TalkParam(name = "Verb") String verb, @TalkParam(name = "Price") double price,
                           @TalkParam(name = "Qty") double qty, @TalkParam(name = "Trader") String trader) throws MkvException;

    @TalkFunction
    void pushUserAction(@TalkParam(name = "code") int code, @TalkParam(name = "msg") String msg) throws MkvException;
}
