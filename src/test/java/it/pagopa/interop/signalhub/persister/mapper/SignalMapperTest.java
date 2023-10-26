package it.pagopa.interop.signalhub.persister.mapper;

import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import it.pagopa.interop.signalhub.persister.queue.model.SignalType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;


class SignalMapperTest {
    private SignalMapper signalMapper;
    private Long signalId;
    private String objectId;
    private String correlationId;
    private String eserviceId;
    private String objectType;
    private Long indexSignal;
    private String signalType;


    @BeforeEach
    void preTest() {
        this.setUp();
    }

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

    private void setUp() {
        this.signalId = 0L;
        this.objectId = "OBJ1";
        this.correlationId = "0A";
        this.objectType = "ESERVICE";
        this.eserviceId = "OBJ1";
        this.indexSignal = 0L;
        this.signalType = SignalType.CREATE.toString();
        this.signalMapper = Mappers.getMapper(SignalMapper.class);
    }
}