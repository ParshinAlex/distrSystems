package ds;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class WebClientConf {

    private final DiscoveryClient discoveryClient;

    private final String loggingService;
    private final String messagesService;


    public WebClientConf(DiscoveryClient discoveryClient,
                         @Value("${external.service.logging.name}") String loggingService,
                         @Value("${external.service.messages.name}") String messagesService) {
        this.discoveryClient = discoveryClient;
        this.loggingService = loggingService;
        this.messagesService = messagesService;
    }

    public List<org.springframework.web.reactive.function.client.WebClient> getServiceWebClients(String service) {
        return discoveryClient.getInstances(service)
                .stream()
                .map(instance -> instance.getUri().toString())
                .map(serviceUrl -> org.springframework.web.reactive.function.client.WebClient.builder().baseUrl(serviceUrl).build())
                .collect(Collectors.toList());
    }

    @LoadBalanced
    @Bean
    @Qualifier("messagesServiceWebClients")
    List<org.springframework.web.reactive.function.client.WebClient> messagesServiceWebClient() {
        return getServiceWebClients(messagesService);
    }

    @LoadBalanced
    @Bean
    @Qualifier("loggingServiceWebClients")
    List<org.springframework.web.reactive.function.client.WebClient> loggingServiceWebClient() {
        return getServiceWebClients(loggingService);
    }
}

