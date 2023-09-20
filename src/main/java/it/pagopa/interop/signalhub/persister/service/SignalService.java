package it.pagopa.interop.signalhub.persister.service;

import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import it.pagopa.interop.signalhub.persister.repository.SignalRepository;
import it.pagopa.interop.signalhub.persister.repository.DeadSignalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;


@Service
public class SignalService {
    @Autowired
    private SignalRepository signalRepository;
    @Autowired
    private DeadSignalRepository deadSignalRepository;

    private Mono<Signal> getSignalById(Long signalId) {
        return this.signalRepository.findById(signalId);
    }

    private Mono<Signal> createSignal(final Signal signal) {
        return this.signalRepository.save(signal);
    }

    public Mono<Signal> pushIntoAwsDbMaster(Signal signal) {
        return signalRepository.
                save(signal);
    }
}