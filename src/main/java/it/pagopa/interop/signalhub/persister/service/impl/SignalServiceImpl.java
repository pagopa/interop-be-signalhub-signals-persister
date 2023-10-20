package it.pagopa.interop.signalhub.persister.service.impl;

import it.pagopa.interop.signalhub.persister.entity.DeadSignal;
import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.mapper.DeadSignalMapper;
import it.pagopa.interop.signalhub.persister.repository.DeadSignalRepository;
import it.pagopa.interop.signalhub.persister.repository.SignalRepository;
import it.pagopa.interop.signalhub.persister.service.SignalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import static it.pagopa.interop.signalhub.persister.exception.ExceptionTypeEnum.DUPLICATE_SIGNAL_ERROR;


@Service
@Slf4j
public class SignalServiceImpl implements SignalService {
    @Autowired
    private SignalRepository signalRepository;
    @Autowired
    private DeadSignalRepository deadSignalRepository;
    @Autowired
    private DeadSignalMapper deadSignalMapper;


    @Transactional
    public Mono<Signal> signalServiceFlow(Signal signal) {

        return getSignalById(signal.getSignalId(), signal.getEserviceId())
                .switchIfEmpty(Mono.just(signal))
                .filter(entity -> entity.getId() == null)
                .flatMap(this::createSignal)
                .switchIfEmpty(Mono.defer(() -> saveToDeadSignal(signal)));
    }

    private Mono<Signal> getSignalById(Long signalId, String eserviceId) { return this.signalRepository.findByIndexSignalAndEserviceId(signalId, eserviceId); }
    private Mono<Signal> createSignal(Signal signal) { return this.signalRepository.save(signal); }
    private Mono<DeadSignal> createDeadSignal(DeadSignal signal) { return this.deadSignalRepository.save(signal); }

    private Mono<DeadSignal> getDeadSignal(Signal signal) {
        DeadSignal deadSignal = deadSignalMapper.signalToDeadSignal(signal);
        deadSignal.setErrorReason(DUPLICATE_SIGNAL_ERROR.toString());
        return Mono.just(deadSignal);
    }

    private Mono<Signal> saveToDeadSignal(Signal signal) {
        return getDeadSignal(signal)
                .flatMap(this::createDeadSignal)
                .then(Mono.just(signal));
    }
}