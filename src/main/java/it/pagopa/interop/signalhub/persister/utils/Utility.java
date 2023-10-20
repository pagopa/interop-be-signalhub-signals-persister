package it.pagopa.interop.signalhub.persister.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.pagopa.interop.signalhub.persister.exception.PDNDGenericException;
import lombok.extern.slf4j.Slf4j;
import static it.pagopa.interop.signalhub.persister.exception.ExceptionTypeEnum.MAPPER_ERROR;


@Slf4j
public class Utility {
    private Utility() {
        throw new IllegalCallerException();
    }

    public static <T> T jsonToObject(String json, Class<T> tClass){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException exception) {
            log.error("exception = {}, errorReason = {}, Error during mapping an object", exception, exception.getMessage());
            throw new PDNDGenericException(MAPPER_ERROR, MAPPER_ERROR.getMessage());
        }
    }
}