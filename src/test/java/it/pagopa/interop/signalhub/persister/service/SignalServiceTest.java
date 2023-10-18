package it.pagopa.interop.signalhub.persister.service;

import it.pagopa.interop.signalhub.persister.entity.DeadSignal;
import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.mapper.DeadSignalMapper;
import it.pagopa.interop.signalhub.persister.queue.model.SignalType;
import it.pagopa.interop.signalhub.persister.repository.DeadSignalRepository;
import it.pagopa.interop.signalhub.persister.repository.SignalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class SignalServiceTest {
    @InjectMocks
    SignalService signalService;
    @Mock
    private SignalRepository signalRepository;
    @Mock
    private DeadSignalRepository deadSignalRepository;
    @Mock
    private DeadSignalMapper deadSignalMapper;

    private final Long id = 0L;
    private final Long signalId = 100L;
    private final String objectId = "OBJ1";
    private final String correlationId = "0A";
    private final String eserviceId = "OBJ1";
    private final String objectType = "0E";
    private final String signalType = SignalType.CREATE.toString();


    @Test
    void signalServiceFlowTest() {
        Signal signalToSave = getSignal();
        signalToSave.setId(null);
        Mockito
                .when(signalRepository.findByIndexSignalAndEserviceId(signalToSave.getSignalId(), signalToSave.getEserviceId()))
                .thenReturn(Mono.empty());

        Mockito
                .when(signalRepository.save(signalToSave))
                .thenReturn(Mono.just(signalToSave));


        signalService.signalServiceFlow(signalToSave)
                .flatMap(sig -> {
                            Assertions.assertNotNull(sig);
                            Assertions.assertEquals(signalToSave, sig);
                    return Mono.empty();
                        }
                ).block();


        ArgumentCaptor<String> captorString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> captorLong = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(signalRepository, Mockito.timeout(1000).times(1))
                .findByIndexSignalAndEserviceId(captorLong.capture(), captorString.capture());

        assertNotNull(captorLong.getValue());
        assertNotNull(captorString.getValue());

        assertEquals(signalToSave.getSignalId(),
                Objects.requireNonNull(captorLong.getValue()));

        assertEquals(signalToSave.getEserviceId(),
                Objects.requireNonNull(captorString.getValue()));


        ArgumentCaptor<Signal> signalArgumentCaptor = ArgumentCaptor.forClass(Signal.class);
        Mockito.verify(signalRepository, Mockito.timeout(1000).times(1))
                .save(signalArgumentCaptor.capture());

        assertNotNull(signalArgumentCaptor.getValue());
        assertEquals(signalToSave,
                Objects.requireNonNull(signalArgumentCaptor.getValue()));
    }


    @Test
    void signalServiceFlowDuplicateSignalTest() {
        Signal signalToUpdate = getSignal();
        Mockito
                .when(signalRepository.findByIndexSignalAndEserviceId(signalToUpdate.getSignalId(), signalToUpdate.getEserviceId()))
                .thenReturn(Mono.just(signalToUpdate));

        DeadSignalMapper dsm = Mappers.getMapper(DeadSignalMapper.class);
        DeadSignal deadSignal = dsm.signalToDeadSignal(signalToUpdate);
        Mockito
                .when(deadSignalMapper.signalToDeadSignal(signalToUpdate))
                .thenReturn(deadSignal);

        Mockito
                .when(deadSignalRepository.save(deadSignal))
                .thenReturn(Mono.empty());

        signalService.signalServiceFlow(signalToUpdate)
                .flatMap(sig -> {
                            Assertions.assertNotNull(sig);
                            Assertions.assertEquals(signalToUpdate, sig);
                        return Mono.empty();
                    }
                ).block();


        ArgumentCaptor<String> captorString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> captorLong = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(signalRepository, Mockito.timeout(1000).times(1))
                .findByIndexSignalAndEserviceId(captorLong.capture(), captorString.capture());

        assertNotNull(captorLong.getValue());
        assertNotNull(captorString.getValue());

        assertEquals(signalToUpdate.getSignalId(),
                Objects.requireNonNull(captorLong.getValue()));

        assertEquals(signalToUpdate.getEserviceId(),
                Objects.requireNonNull(captorString.getValue()));


        ArgumentCaptor<Signal> signalArgumentCaptor = ArgumentCaptor.forClass(Signal.class);
        Mockito.verify(deadSignalMapper, Mockito.timeout(1000).times(1))
                .signalToDeadSignal(signalArgumentCaptor.capture());

        assertNotNull(signalArgumentCaptor.getValue());
        assertEquals(signalToUpdate,
                Objects.requireNonNull(signalArgumentCaptor.getValue()));


        ArgumentCaptor<DeadSignal> deadSignalArgumentCaptor = ArgumentCaptor.forClass(DeadSignal.class);
        Mockito.verify(deadSignalRepository, Mockito.timeout(1000).times(1))
                .save(deadSignalArgumentCaptor.capture());

        assertNotNull(deadSignalArgumentCaptor.getValue());
        assertEquals(deadSignal,
                Objects.requireNonNull(deadSignalArgumentCaptor.getValue()));
    }

    private Signal getSignal() {
        Signal signal = new Signal();
        signal.setId(this.id);
        signal.setSignalId(this.signalId);
        signal.setSignalType(this.signalType);
        signal.setObjectType(this.objectType);
        signal.setCorrelationId(this.correlationId);
        signal.setEserviceId(this.eserviceId);
        signal.setObjectId(this.objectId);
        return signal;
    }
}