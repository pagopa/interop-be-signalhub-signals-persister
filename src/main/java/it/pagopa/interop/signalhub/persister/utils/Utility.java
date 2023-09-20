package it.pagopa.interop.signalhub.persister.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utility {

    private Utility() {
        throw new IllegalCallerException();
    }

    public static <T> T jsonToObject(ObjectMapper objectMapper, String json, Class<T> tClass){
        try {
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException exception) {
            log.error("exception = {}, errorReason = {}, Error during mapping an object", exception, exception.getMessage());
            return null;
        }
    }

}
