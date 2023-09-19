package it.pagopa.interop.signalhub.persister.queue.consumer;


import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Slf4j
public class SqsInternalListener {

    public CompletableFuture<Void> listen(String node, @Headers Map<String, Object> headers) {
        return CompletableFuture.completedFuture(null);
    }

}
