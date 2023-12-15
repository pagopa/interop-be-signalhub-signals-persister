package it.pagopa.interop.signalhub.persister.repository;

import it.pagopa.interop.signalhub.persister.entity.DeadSignal;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeadSignalRepository extends ReactiveCrudRepository<DeadSignal, Long> {
}
