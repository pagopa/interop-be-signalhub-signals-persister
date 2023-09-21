package it.pagopa.interop.signalhub.persister.queue.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.exception.PdndGenericException;
import it.pagopa.interop.signalhub.persister.mapper.SignalMapper;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import it.pagopa.interop.signalhub.persister.service.SignalService;
import it.pagopa.interop.signalhub.persister.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static it.pagopa.interop.signalhub.persister.exception.ExceptionTypeEnum.MAPPER_ERROR;


@Slf4j
@Component
public class SqsInternalListener {
    @Autowired
    private SignalService signalService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SignalMapper signalMapper;

    @SqsListener(value = "${aws.internal-queue-name}")
    public CompletableFuture<Void> pullFromAwsInternalQueue(@Payload String node, @Headers Map<String, Object> headers) {
        log.info("payloadBody: {}, headers: {}, PullFromInternalQueue received input", node, headers);
        SignalEvent signalEvent = convertToObject(node, SignalEvent.class);
        Signal signal = signalMapper.signalEventToSignal(signalEvent);

        return signalService
                .pushIntoAwsDbMaster(signal)
                .then()
                .toFuture();
    }

    private <T> T convertToObject(String body, Class<T> tClass){
        T entity = Utility.jsonToObject(this.objectMapper, body, tClass);
        if (entity == null) {
            log.error("errorReason = {}, An error occurred during object conversion", MAPPER_ERROR.getMessage());
            throw new PdndGenericException(MAPPER_ERROR, MAPPER_ERROR.getMessage());
        }
        return entity;
    }
}
