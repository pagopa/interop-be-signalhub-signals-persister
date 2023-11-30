package it.pagopa.interop.signalhub.persister.config;

import io.awspring.cloud.sqs.config.SqsBootstrapConfiguration;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import java.net.URI;


@Import(SqsBootstrapConfiguration.class)
@Configuration
@Slf4j
public class AwsBeanBuilder {
    private final AwsPropertiesConfig props;

    public AwsBeanBuilder(AwsPropertiesConfig props) {
        this.props = props;
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return configureBuilder(SqsAsyncClient.builder(), props.getSqsEndpoint());
    }

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory
                .builder()
                .configure(options -> options
                        //.acknowledgementMode(AcknowledgementMode.ON_SUCCESS)
                        .maxConcurrentMessages(10)
                        .maxMessagesPerPoll(10))
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }

    private <C> C configureBuilder(AwsClientBuilder<?, C> builder, String endpoint) {
        if( props != null ) {

            String profileName = props.getProfile();
            if( StringUtils.isNotBlank( profileName ) ) {
                builder.credentialsProvider( ProfileCredentialsProvider.create( profileName ) );
            } else {
                log.debug("Using WebIdentityTokenFileCredentialsProvider");
                builder.credentialsProvider( WebIdentityTokenFileCredentialsProvider.create() );
            }

            String regionCode = props.getRegion();
            if( StringUtils.isNotBlank( regionCode )) {
                log.debug("Setting region to: {}", regionCode);
                builder.region( Region.of( regionCode ));
            }

            if( StringUtils.isNotBlank( endpoint )) {
                builder.endpointOverride(URI.create(endpoint));
            }

        }

        return builder.build();
    }
}