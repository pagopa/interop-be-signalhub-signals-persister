package it.pagopa.interop.signalhub.persister.mapper;

import it.pagopa.interop.signalhub.persister.entity.DeadSignal;
import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.queue.model.SignalType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;


class DeadSignalMapperTest {
    private final DeadSignalMapper deadSignalMapper = Mappers.getMapper(DeadSignalMapper.class);
    private final Long signalId = 0L;
    private final String objectId = "OBJ1";
    private final String correlationId = "0A";
    private final String eserviceId = "OBJ1";
    private final String objectType = "0E";
    private final Long indexSignal = 0L;
    private final String errorReason = "404 Not Found";
    private final String signalType = SignalType.CREATE.toString();



    @Test
    void signalToDeadSignalTest() {
        Signal signal = getSignal();
        DeadSignal deadSignal = deadSignalMapper.signalToDeadSignal(signal);
        assertNotNull(deadSignal);
        assertEquals(deadSignal.getSignalId(), this.indexSignal);
        assertEquals(deadSignal.getObjectId(), this.objectId);
        assertEquals(deadSignal.getEserviceId(), this.eserviceId);
        assertEquals(deadSignal.getCorrelationId(), this.correlationId);
        assertEquals(deadSignal.getSignalType(), this.signalType);
        assertEquals(deadSignal.getObjectType(), this.objectType);
    }

    @Test
    void signalToDeadSignalNullCaseTest() {
        DeadSignal deadSignal = deadSignalMapper.signalToDeadSignal(null);
        assertNull(deadSignal);
    }

    @Test
    void signalToDeadSignalWithSignalTypeNullTest() {
        Signal signal = getSignal();
        signal.setSignalType(null);
        DeadSignal deadSignal = deadSignalMapper.signalToDeadSignal(signal);
        assertNotNull(deadSignal);
        assertNull(deadSignal.getSignalType());
    }

    @Test
    void deadSignalToSignalTest() {
        DeadSignal deadSignal = getDeadSignal();
        Signal signal = deadSignalMapper.deadSignalToSignal(deadSignal);
        assertNotNull(signal);
        assertEquals(signal.getSignalId(), this.indexSignal);
        assertEquals(signal.getObjectId(), this.objectId);
        assertEquals(signal.getEserviceId(), this.eserviceId);
        assertEquals(signal.getCorrelationId(), this.correlationId);
        assertEquals(signal.getSignalType(), this.signalType);
        assertEquals(signal.getObjectType(), this.objectType);
    }

    @Test
    void deadSignalToSignalNullCaseTest() {
        Signal signal = deadSignalMapper.deadSignalToSignal(null);
        assertNull(signal);
    }

    @Test
    void deadSignalToSignalWithSignalTypeNullTest() {
        DeadSignal deadSignal = getDeadSignal();
        deadSignal.setSignalType(null);
        Signal signal = deadSignalMapper.deadSignalToSignal(deadSignal);
        assertNotNull(signal);
        assertNull(signal.getSignalType());
    }

    private Signal getSignal() {
        Signal signal = new Signal();
        signal.setSignalId(this.signalId);
        signal.setSignalType(this.signalType);
        signal.setObjectType(this.objectType);
        signal.setCorrelationId(this.correlationId);
        signal.setEserviceId(this.eserviceId);
        signal.setObjectId(this.objectId);
        return signal;
    }

    private DeadSignal getDeadSignal() {
        DeadSignal deadSignal = new DeadSignal();
        deadSignal.setSignalType(this.signalType);
        deadSignal.setObjectType(this.objectType);
        deadSignal.setEserviceId(this.eserviceId);
        deadSignal.setObjectId(this.objectId);
        deadSignal.setCorrelationId(this.correlationId);
        deadSignal.setSignalId(this.signalId);
        deadSignal.setErrorReason(this.errorReason);
        return deadSignal;
    }
}