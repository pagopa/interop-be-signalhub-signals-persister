package it.pagopa.interop.signalhub.persister;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(LocalStackTestConfig.class)
@SpringBootTest(classes = SignalHubSignalsPersisterApplication.class)

@ActiveProfiles("test")
class ApplicationTest {


    @Test
    void testApp(){
        assertTrue(true);
    }

}
