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
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@AllArgsConstructor
public class SqsInternalListener {
    private SignalService signalService;
    private SignalMapper signalMapper;


    @SqsListener(value = "${aws.internal-queue-name}")
    public CompletableFuture<Void> pullFromAwsInternalQueue(@Payload String node, @Headers Map<String, Object> headers) {
        log.info("payloadBody: {}, headers: {}, PullFromInternalQueue received input", node, headers);
        String correlationId = (String) headers.get(SignalMapper.CORRELATION_ID_HEADER_KEY);

        return Mono.just(node)
                .map(json -> Utility.jsonToObject(node, SignalEvent.class))
                .map((signalEvent) -> signalMapper.signalEventToSignal(signalEvent, correlationId))
                .flatMap(signalService::signalServiceFlow)
                .then()
                .toFuture();
    }
}