package it.pagopa.interop.signalhub.persister.utility;

import it.pagopa.interop.signalhub.persister.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import it.pagopa.interop.signalhub.persister.queue.model.SignalType;
import it.pagopa.interop.signalhub.persister.utils.Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static it.pagopa.interop.signalhub.persister.exception.ExceptionTypeEnum.MAPPER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class UtilityTest {
    private final Utility utility = new Utility();
    @Test
    void jsonToObjectTest() {
        String jsonNode = """
                {
                    "signalType": "CREATE",
                    "objectId": "OBJ1",
                    "objectType": "T1",
                    "eserviceId": "E1",
                    "indexSignal": "000001"
                }
                """;
        SignalEvent signalEvent = Utility.jsonToObject(jsonNode, SignalEvent.class);
        assertNotNull(signalEvent);
        assertEquals(SignalType.CREATE, signalEvent.getSignalType());
        assertEquals("OBJ1", signalEvent.getObjectId());
        assertEquals("T1", signalEvent.getObjectType());
        assertEquals("E1", signalEvent.getEserviceId());
        assertEquals("1", signalEvent.getIndexSignal().toString());
    }

    @Test
    void jsonToObjectExceptionErrorTest() {
        String jsonNode = "@sa1e43r@dgr@°#cv-dsw?!";
        PDNDGenericException exception =
                Assertions.assertThrows(PDNDGenericException.class,
                        () -> Utility.jsonToObject(jsonNode, SignalEvent.class));

        assertEquals(MAPPER_ERROR.getMessage(), exception.getMessage());
    }
}