package it.pagopa.interop.signalhub.persister.utility;

import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import it.pagopa.interop.signalhub.persister.queue.model.SignalType;
import it.pagopa.interop.signalhub.persister.utils.Utility;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class UtilityTest {
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
}