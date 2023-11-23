package it.pagopa.interop.signalhub.persister.mapper;

import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface SignalMapper {
    String CORRELATION_ID_HEADER_KEY = "correlationId";

    SignalEvent signalToSignalEvent(Signal signal);

    @Mapping(target = "correlationId", source = "correlationId")
    Signal signalEventToSignal(SignalEvent signalEvent, String correlationId);
}
