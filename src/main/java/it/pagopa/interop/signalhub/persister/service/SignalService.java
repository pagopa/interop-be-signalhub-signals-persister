package it.pagopa.interop.signalhub.persister.service;

import it.pagopa.interop.signalhub.persister.entity.Signal;
import reactor.core.publisher.Mono;

public interface SignalService {
    Mono<Signal> signalServiceFlow(Signal signal);
}
