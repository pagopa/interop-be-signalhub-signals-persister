package it.pagopa.interop.signalhub.persister.queue.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class InternalSqsProducer implements SqsProducer<SignalEvent> {
    private SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public Mono<SignalEvent> push(SignalEvent event) {
        return Mono.fromFuture(this.sqsTemplate.sendAsync(
                //MessageBuilder.withPayload(convertToJson(event))
        )).thenReturn(event);
    }

}
