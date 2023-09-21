package it.pagopa.interop.signalhub.persister.mapper;

import it.pagopa.interop.signalhub.persister.entity.DeadSignal;
import it.pagopa.interop.signalhub.persister.entity.Signal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeadSignalMapper {
    DeadSignal signalToDeadSignal(Signal signal);
    Signal deadSignalToSignal(DeadSignal deadSignal);
}
