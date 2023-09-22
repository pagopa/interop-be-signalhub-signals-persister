package it.pagopa.interop.signalhub.persister.service;

import it.pagopa.interop.signalhub.persister.entity.DeadSignal;
import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.exception.PdndGenericException;
import it.pagopa.interop.signalhub.persister.mapper.DeadSignalMapper;
import it.pagopa.interop.signalhub.persister.mapper.SignalMapper;
import it.pagopa.interop.signalhub.persister.queue.producer.InternalSqsProducer;
import it.pagopa.interop.signalhub.persister.repository.DeadSignalRepository;
import it.pagopa.interop.signalhub.persister.repository.SignalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static it.pagopa.interop.signalhub.persister.exception.ExceptionTypeEnum.DUPLICATE_SIGNAL_ERROR;


@Service
@Slf4j
public class SignalService {
    @Autowired
    private SignalRepository signalRepository;
    @Autowired
    private DeadSignalRepository deadSignalRepository;
    @Autowired
    private SignalMapper signalMapper;
    @Autowired
    private DeadSignalMapper deadSignalMapper;
    @Autowired
    private InternalSqsProducer internalSqsProducer;


    private Mono<Signal> getSignalById(Long signalId, String eserviceId) { return this.signalRepository.findByIndexSignalAndEserviceId(signalId, eserviceId); }

    private Mono<Signal> createSignal(final Signal signal) {
        return this.signalRepository.save(signal);
    }

    @Transactional
    public Mono<Signal> pushIntoAwsDbMaster(Signal signal) {
        return getSignalById(signal.getSignalId(), signal.getEserviceId())
                .switchIfEmpty(Mono.just(signal))
                .flatMap(entity -> {
                    if(entity.getId() == null) {
                        return createSignal(signal)
                                .doOnNext(entitySaved -> log.info("signalEntity = {}, Signal entity created", entitySaved));
                    }
                    log.info("signal = {}, The searched entity is not present in the Database", signal);
                    return Mono.error(new PdndGenericException(DUPLICATE_SIGNAL_ERROR, DUPLICATE_SIGNAL_ERROR.getMessage()));
                })
                .onErrorResume(exception -> {
                    pushDeadSignalDlsQueue(signal);
                    return Mono.empty();
                });
    }

    private void pushDeadSignalDlsQueue(Signal signal) {
        DeadSignal deadSignal = deadSignalMapper.signalToDeadSignal(signal);
        deadSignal.setError(DUPLICATE_SIGNAL_ERROR.toString());
        deadSignalRepository.save(deadSignal)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(entitySaved -> log.info("signal = {}, The searched entity is already present into Signal's table, it has been saved into DeadSignal's table", entitySaved))
                .flatMap(entitySaved -> {
                    log.info("signal = {}, The signal has been pushed on dlq queue", signal);
                    return internalSqsProducer.push(signalMapper.signalToSignalEvent(signal));
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }
}