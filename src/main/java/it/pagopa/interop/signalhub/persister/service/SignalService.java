package it.pagopa.interop.signalhub.persister.service;

import it.pagopa.interop.signalhub.persister.entity.DeadSignal;
import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.exception.PdndGenericException;
import it.pagopa.interop.signalhub.persister.mapper.DeadSignalMapper;
import it.pagopa.interop.signalhub.persister.repository.SignalRepository;
import it.pagopa.interop.signalhub.persister.repository.DeadSignalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static it.pagopa.interop.signalhub.persister.exception.ExceptionTypeEnum.DUPLICATE_SIGNAL_ERROR;


@Service
@Slf4j
public class SignalService {
    @Autowired
    private SignalRepository signalRepository;
    @Autowired
    private DeadSignalRepository deadSignalRepository;
    @Autowired
    private DeadSignalMapper deadSignalMapper;

    private Mono<Signal> getSignalById(Long signalId) {
        return this.signalRepository.findById(signalId);
    }

    private Mono<Signal> createSignal(final Signal signal) {
        return this.signalRepository.save(signal);
    }

    public Mono<Signal> pushIntoAwsDbMaster(Signal signal) {
        return signalRepository
                .findByIndexSignalAndEserviceId(signal.getSignalId(), signal.getEserviceId())
                .switchIfEmpty(Mono.just(signal))
                .flatMap(entity -> {
                    if(entity.getId() == null) {
                        return signalRepository.save(signal)
                                .doOnNext(entitySaved -> log.info("signalEntity = {}, Signal entity created", entitySaved));
                    }
                    log.info("signal = {}, The searched entity is not present in the Database", signal);
                    return Mono.error(new PdndGenericException(DUPLICATE_SIGNAL_ERROR, DUPLICATE_SIGNAL_ERROR.getMessage()));
                })
                .onErrorResume(exception -> {
                    DeadSignal deadSignal = deadSignalMapper.signalToDeadSignal(signal);
                    deadSignal.setError(DUPLICATE_SIGNAL_ERROR.toString());
                    return deadSignalRepository.save(deadSignal)
                            .doOnNext(entitySaved -> log.info("signal = {}, The searched entity is already present into Signal's table, it has been saved into DeadSignal's table", entitySaved))
                            .thenReturn(signal);
                });
    }
}