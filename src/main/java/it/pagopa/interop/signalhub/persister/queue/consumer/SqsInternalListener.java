package it.pagopa.interop.signalhub.persister.queue.consumer;


import io.awspring.cloud.sqs.annotation.SqsListener;
import it.pagopa.interop.signalhub.persister.mapper.SignalMapper;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import it.pagopa.interop.signalhub.persister.service.SignalService;
import it.pagopa.interop.signalhub.persister.utils.Utility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static it.pagopa.interop.signalhub.persister.utils.Const.TRACE_ID_KEY;


@Slf4j
@Component
@AllArgsConstructor
public class SqsInternalListener {
    private SignalService signalService;
    private SignalMapper signalMapper;


    @SqsListener(value = "${aws.internal-queue-name}")
    public CompletableFuture<Void> pullFromAwsInternalQueue(@Payload String node, @Headers Map<String, Object> headers) {
        String correlationId = (String) headers.get(SignalMapper.CORRELATION_ID_HEADER_KEY);

        return Mono.just(node)
                .contextWrite(Context.of(TRACE_ID_KEY, correlationId))
                .doOnNext(json -> log.info("payloadBody: {}, headers: {}, PullFromInternalQueue received input", node, headers))
                .map(json -> Utility.jsonToObject(node, SignalEvent.class))
                .map(signalEvent -> signalMapper.signalEventToSignal(signalEvent, correlationId))
                .flatMap(signalEvent -> signalService.signalServiceFlow(signalEvent)
                        .contextWrite(Context.of(TRACE_ID_KEY, correlationId))
                )
                .then()
                .toFuture();
    }
}