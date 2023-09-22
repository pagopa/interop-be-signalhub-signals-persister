package it.pagopa.interop.signalhub.persister.queue.producer;

import reactor.core.publisher.Mono;

public interface SqsProducer<T> {

    Mono<T> push(T event);

}
