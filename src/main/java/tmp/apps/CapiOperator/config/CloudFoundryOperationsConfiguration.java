package tmp.apps.CapiOperator.config;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.ProxyConfiguration;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.UaaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Optional;

@Configuration
@EnableAutoConfiguration
public class CloudFoundryOperationsConfiguration {

    private DebugProperties debugProperties;

    public CloudFoundryOperationsConfiguration(DebugProperties debugProperties) {
        this.debugProperties = debugProperties;
    }

    @Bean
    ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

    @Bean
    @Lazy
    DefaultCloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cloudFoundryClient,
                                                         DopplerClient dopplerClient,
                                                         UaaClient uaaClient,
                                                         @Value("${cf.organization}") String organization,
                                                         @Value("${cf.space}") String space) {
        return DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(cloudFoundryClient)
                .dopplerClient(dopplerClient)
                .uaaClient(uaaClient)
                .organization(organization)
                .space(space)
                .build();
    }

    @Bean
    DefaultConnectionContext connectionContext(@Value("${cf.apiHost}") String apiHost) {
        return DefaultConnectionContext.builder()
                .apiHost(apiHost)
                .additionalHttpClientConfiguration(httpClient -> httpClient.wiretap(debugProperties.isWiretap()))
                .proxyConfiguration(debugProperties.getProxy().isActive() ? getProxy() : Optional.empty())
                .skipSslValidation(debugProperties.isSkipssl())
                .build();
    }

    private Optional<ProxyConfiguration> getProxy() {
        return Optional.of(ProxyConfiguration.builder()
                .host(debugProperties.getProxy().getHost())
                .port(debugProperties.getProxy().getPort())
                .build());
    }

    @Bean
    ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorDopplerClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

    @Bean
    PasswordGrantTokenProvider tokenProvider(@Value("${cf.password}") String password,
                                             @Value("${cf.username}") String username) {

        return PasswordGrantTokenProvider.builder()
                .password(password)
                .username(username)
                .build();
    }

    @Bean
    ReactorUaaClient uaaClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorUaaClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

}
