package it.pagopa.interop.signalhub.persister.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PdndGenericExceptionTest {

    @Test
    void exceptionTest() {
        assertDoesNotThrow(() -> new PdndGenericException(ExceptionTypeEnum.MAPPER_ERROR, ExceptionTypeEnum.MAPPER_ERROR.getMessage()));
        assertDoesNotThrow(() -> new PdndGenericException(ExceptionTypeEnum.DUPLICATE_SIGNAL_ERROR, ExceptionTypeEnum.DUPLICATE_SIGNAL_ERROR.getMessage()));
    }
}
