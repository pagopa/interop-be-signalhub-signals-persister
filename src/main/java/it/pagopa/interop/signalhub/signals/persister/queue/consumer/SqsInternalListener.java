package it.pagopa.interop.signalhub.signals.persister.queue.consumer;


import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
public class SqsInternalListener {


    @SqsListener
    public CompletableFuture<Void> listen(String node, @Headers Map<String, Object> headers) {
        return CompletableFuture.completedFuture(null);
    }

}
