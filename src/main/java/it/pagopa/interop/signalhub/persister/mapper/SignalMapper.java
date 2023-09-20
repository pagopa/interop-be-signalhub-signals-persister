package it.pagopa.interop.signalhub.persister.mapper;

import it.pagopa.interop.signalhub.persister.entity.Signal;
import it.pagopa.interop.signalhub.persister.queue.model.SignalEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface SignalMapper {
    @Mapping(target = "indexSignal", source = "signal.signalId")
    SignalEvent signalToSignalEvent(Signal signal);
    @Mapping(target = "signalId", source = "signalEvent.indexSignal")
    Signal signalEventToSignal(SignalEvent signalEvent);
}
