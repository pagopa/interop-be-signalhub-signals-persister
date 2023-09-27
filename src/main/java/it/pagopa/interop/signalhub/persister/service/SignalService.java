package it.pagopa.interop.signalhub.persister.service;

import it.pagopa.interop.signalhub.persister.entity.DeadSignal;
import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.mapper.DeadSignalMapper;
import it.pagopa.interop.signalhub.persister.repository.DeadSignalRepository;
import it.pagopa.interop.signalhub.persister.repository.SignalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

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



    private Mono<Signal> getSignalById(Long signalId, String eserviceId) { return this.signalRepository.findByIndexSignalAndEserviceId(signalId, eserviceId); }

    private Mono<Signal> createSignal(Signal signal) {
        return this.signalRepository.save(signal);
    }


    @Transactional
    public Mono<Signal> pushIntoAwsDbMaster(Signal signal) {

        return getSignalById(signal.getSignalId(), signal.getEserviceId())
                .switchIfEmpty(Mono.just(signal))
                .filter(entity -> entity.getId() == null)
                .flatMap(this::createSignal)
                .switchIfEmpty(Mono.defer(() -> saveToDeadSignal(signal)));
    }


    private Mono<DeadSignal> getDeadSignal(Signal signal) {
        DeadSignal deadSignal = deadSignalMapper.signalToDeadSignal(signal);
        deadSignal.setError(DUPLICATE_SIGNAL_ERROR.toString());
        return Mono.just(deadSignal);
    }

    private Mono<Signal> saveToDeadSignal(Signal signal) {
        return getDeadSignal(signal)
                .flatMap(this.deadSignalRepository::save)
                .then(Mono.just(signal));

    }
}