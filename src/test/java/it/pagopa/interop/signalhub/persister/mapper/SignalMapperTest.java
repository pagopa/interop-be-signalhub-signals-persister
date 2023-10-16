package it.pagopa.interop.signalhub.persister.mapper;

import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import it.pagopa.interop.signalhub.persister.queue.model.SignalType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;


class SignalMapperTest {
    private final SignalMapper signalMapper = Mappers.getMapper(SignalMapper.class);
    private final Long signalId = 0L;
    private final String objectId = "OBJ1";
    private final String correlationId = "0A";
    private final String eserviceId = "OBJ1";
    private final String objectType = "0E";
    private final Long indexSignal = 0L;
    private final String signalType = SignalType.CREATE.toString();


    @Test
    void signalToSignalEventTest() {
        Signal signal = getSignal();
        SignalEvent signalEvent = signalMapper.signalToSignalEvent(signal);
        assertNotNull(signalEvent);
        assertEquals(signalEvent.getObjectId(), this.objectId);
        assertEquals(signalEvent.getEserviceId(), this.eserviceId);
        assertEquals(signalEvent.getSignalType(), SignalType.CREATE);
        assertEquals(signalEvent.getObjectType(), this.objectType);
        assertEquals(signalEvent.getIndexSignal(), this.indexSignal);
    }

    @Test
    void signalToSignalEventNullCaseTest() {
        SignalEvent signalEvent = signalMapper.signalToSignalEvent(null);
        assertNull(signalEvent);
    }

    @Test
    void signalToSignalEventWithSignalTypeNullTest() {
        Signal signal = getSignal();
        signal.setSignalType(null);
        SignalEvent signalEvent = signalMapper.signalToSignalEvent(signal);
        assertNotNull(signalEvent);
        assertNull(signalEvent.getSignalType());
    }

    @Test
    void signalEventToSignalTest() {
        SignalEvent signalEvent = getSignalEvent();
        Signal signal = signalMapper.signalEventToSignal(signalEvent, this.correlationId);
        assertNotNull(signal);
        assertEquals(signal.getSignalId(), this.indexSignal);
        assertEquals(signal.getObjectId(), this.objectId);
        assertEquals(signal.getEserviceId(), this.eserviceId);
        assertEquals(signal.getCorrelationId(), this.correlationId);
        assertEquals(signal.getSignalType(), this.signalType);
        assertEquals(signal.getObjectType(), this.objectType);
    }

    @Test
    void signalEventToSignalNullCaseTest() {
        Signal signal = signalMapper.signalEventToSignal(null, null);
        assertNull(signal);

        signal = signalMapper.signalEventToSignal(null, this.correlationId);
        assertNotNull(signal);

        SignalEvent signalEvent = getSignalEvent();
        signal = signalMapper.signalEventToSignal(signalEvent, null);
        assertNotNull(signal);
        assertEquals(signal.getSignalId(), this.indexSignal);
        assertEquals(signal.getObjectId(), this.objectId);
        assertEquals(signal.getEserviceId(), this.eserviceId);
        assertEquals(signal.getObjectType(), this.objectType);
    }

    @Test
    void signalEventToSignalWithSignalTypeNullTest() {
        SignalEvent signalEvent = getSignalEvent();
        signalEvent.setSignalType(null);
        Signal signal = signalMapper.signalEventToSignal(signalEvent, this.correlationId);
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

    private SignalEvent getSignalEvent() {
        SignalEvent signalEvent = new SignalEvent();
        signalEvent.setSignalType(SignalType.CREATE);
        signalEvent.setObjectType(this.objectType);
        signalEvent.setEserviceId(this.eserviceId);
        signalEvent.setObjectId(this.objectId);
        signalEvent.setIndexSignal(this.indexSignal);
        return signalEvent;
    }
}