package it.pagopa.interop.signalhub.persister.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "poc.signal-hub")
public class SignalHubPersisterConfig {
    private String internalQueueName;
}
