package com.activity.bondprice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iontrading.isf.commons.async.AsyncResult;
import com.iontrading.mkv.exceptions.MkvException;
import com.iontrading.talk.api.annotation.TalkFunction;
import com.iontrading.talk.api.annotation.TalkResult;

import java.util.concurrent.ExecutionException;

public interface MQInterface {
    @TalkFunction(result = @TalkResult(name = "MyCOOLQueue",overview = true))
    AsyncResult<String> openQueue() throws MkvException;

    @TalkFunction(name="AddDataToQueue")
    AsyncResult<Void> pushData() throws JsonProcessingException, ExecutionException, InterruptedException, MkvException;
}
