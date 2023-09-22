package it.pagopa.interop.signalhub.persister.queue.producer;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import it.pagopa.interop.signalhub.persister.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@AllArgsConstructor
public class InternalSqsProducer implements SqsProducer<SignalEvent> {
    private SqsTemplate sqsTemplate;


    @Override
    public Mono<SignalEvent> push(SignalEvent event) {
        String json = Utility.objectToJson(event);
        if(json == null) {
            return null;
        } else {
            Message<String> message = MessageBuilder.withPayload(json).build();
            return Mono.fromFuture(this.sqsTemplate.sendAsync(message))
                    .thenReturn(event);
        }
    }

}
