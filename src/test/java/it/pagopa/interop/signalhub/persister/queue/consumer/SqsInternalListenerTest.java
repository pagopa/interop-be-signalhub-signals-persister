package it.pagopa.interop.signalhub.persister.queue.consumer;

import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.persister.mapper.SignalMapper;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import it.pagopa.interop.signalhub.persister.queue.model.SignalType;
import it.pagopa.interop.signalhub.persister.service.SignalService;
import it.pagopa.interop.signalhub.persister.utils.Utility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import static it.pagopa.interop.signalhub.persister.exception.ExceptionTypeEnum.MAPPER_ERROR;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class SqsInternalListenerTest {
    @InjectMocks
    private SqsInternalListener sqsInternalListener;
    @Mock
    private SignalService signalService;
    @Mock
    private SignalMapper signalMapper;
    private MockedStatic<Utility> utility;
    private String objectId;
    private String correlationId;
    private String eserviceId;
    private String objectType;
    private Long signalId;


    @BeforeEach
    void preTest() {
        utility = Mockito.mockStatic(Utility.class);
        this.setUp();
    }

    @AfterEach
    void postTest() {
        if(utility != null) {
            utility.close();
        }
    }

    @Test
    void pullFromAwsInternalQueueTest() throws ExecutionException, InterruptedException {
        String jsoNode = """
                {
                    "signalType": "CREATE",
                    "objectId": "OBJ1",
                    "objectType": "T1",
                    "eserviceId": "E1",
                    "indexSignal": "000001"
                }
                """;
        SignalEvent signalEvent = getSignalEvent();

        utility.when(() -> Utility.jsonToObject(jsoNode, SignalEvent.class))
            .thenReturn(signalEvent);

        Map<String, Object> headers = new HashMap<>();
        headers.put(SignalMapper.CORRELATION_ID_HEADER_KEY, correlationId);

        SignalMapper sm = Mappers.getMapper(SignalMapper.class);
        Signal signal = sm.signalEventToSignal(signalEvent, correlationId);

        Mockito
                .when(signalMapper.signalEventToSignal(signalEvent, correlationId))
                .thenReturn(signal);

        Mockito
                .when(signalService.signalServiceFlow(signal))
                .thenReturn(Mono.just(signal));

        sqsInternalListener.pullFromAwsInternalQueue(jsoNode, headers).get();


        Mockito
                .verify(signalMapper, Mockito.timeout(1000).times(1))
                .signalEventToSignal(signalEvent, correlationId);


        ArgumentCaptor<SignalEvent> signalEventArgumentCaptor = ArgumentCaptor.forClass(SignalEvent.class);
        ArgumentCaptor<String> captorString = ArgumentCaptor.forClass(String.class);

        Mockito
                .verify(signalMapper, Mockito.timeout(1000).times(1))
                .signalEventToSignal(signalEventArgumentCaptor.capture(), captorString.capture());

        assertNotNull(signalEventArgumentCaptor.getValue());
        assertNotNull(captorString.getValue());

        assertEquals(signalEvent,
                Objects.requireNonNull(signalEventArgumentCaptor.getValue()));
        assertEquals(correlationId,
                Objects.requireNonNull(captorString.getValue()));


        Mockito
                .verify(signalService, Mockito.timeout(1000).times(1))
                .signalServiceFlow(signal);
    }

    @Test
    void pullFromAwsInternalQueueJsonNullTest() {
        String jsoNode = null;
        Map<String, Object> headers = new HashMap<>();
        headers.put(SignalMapper.CORRELATION_ID_HEADER_KEY, correlationId);
        Assertions.assertThrows(NullPointerException.class, () ->
                sqsInternalListener.pullFromAwsInternalQueue(jsoNode, headers)
                        .get());
    }

    @Test
    void pullFromAwsInternalQueueExceptionToObjectTest() {
        String jsoNode = """
                {
                    "signalType": "CREATE",
                    "objectId": "OBJ1",
                    "objectType": "T1",
                    "eserviceId": "E1",
                    "indexSignal": "000001"
                }
                """;
        SignalEvent signalEvent = getSignalEvent();

        utility.when(() -> Utility.jsonToObject(jsoNode, SignalEvent.class))
                .thenThrow(new PDNDGenericException(MAPPER_ERROR, MAPPER_ERROR.getMessage()));

        Map<String, Object> headers = new HashMap<>();
        headers.put(SignalMapper.CORRELATION_ID_HEADER_KEY, correlationId);

        ExecutionException exception = Assertions.assertThrows(ExecutionException.class, () ->
                sqsInternalListener.pullFromAwsInternalQueue(jsoNode, headers)
                        .get());

        Mockito.verify(signalMapper, Mockito.timeout(1000).times(0))
                .signalEventToSignal(signalEvent, correlationId);

        Mockito.verify(signalService, Mockito.timeout(1000).times(0))
                .signalServiceFlow(Mockito.any());

        Assertions.assertEquals(MAPPER_ERROR.getMessage(), exception.getCause().getMessage());
    }

    private SignalEvent getSignalEvent() {
        SignalEvent signalEvent = new SignalEvent();
        signalEvent.setSignalType(SignalType.CREATE);
        signalEvent.setObjectType(this.objectType);
        signalEvent.setEserviceId(this.eserviceId);
        signalEvent.setObjectId(this.objectId);
        signalEvent.setSignalId(this.signalId);
        return signalEvent;
    }

    private void setUp() {
        this.objectId = "OBJ1";
        this.correlationId = "0A";
        this.eserviceId = "OBJ1";
        this.objectType = "ESERVICE";
        this.signalId = 0L;
    }
}