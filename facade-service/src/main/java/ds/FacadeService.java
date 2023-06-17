package ds;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class FacadeService {

    private final FacadeKafka facadeKafka;

    private final List<WebClient> loggingWebClients;
    private final List<WebClient> messagesWebClients;

    public FacadeService(FacadeKafka facadeKafka,
                         @Qualifier("loggingServiceWebClients") List<WebClient> loggingWebClients,
                         @Qualifier("messagesServiceWebClients") List<WebClient> messagesWebClients) {
        this.facadeKafka = facadeKafka;
        this.loggingWebClients = loggingWebClients;
        this.messagesWebClients = messagesWebClients;

    }

    private final String LOGGING_URL = "/logging";
    private final String MESSAGES_URL = "/messages";
    private final Random random = new Random();

    public String getMessages() {
        var loggingWebClient = getRandomService(loggingWebClients);
        var messagesWebClient = getRandomService(messagesWebClients);
        var loggingResponse = getMessages(loggingWebClient, LOGGING_URL);
        var messageResponse = getMessages(messagesWebClient, MESSAGES_URL);
        return loggingResponse + " : " + messageResponse;
    }

    public void writeMessage(@RequestBody String body) {
        var msg = new Message(UUID.randomUUID(), body);
        log.info("Message {}", msg);

        var loggingWebClient = getRandomService(loggingWebClients);
        postMessage(loggingWebClient, LOGGING_URL, msg);

        facadeKafka.produce(msg);
    }

    private void postMessage(WebClient serviceWebClient, String uri, Message msg){
        serviceWebClient
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(msg), Message.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private String getMessages(WebClient serviceWebClient, String uri){
        return serviceWebClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private WebClient getRandomService(List<WebClient> webClients) {
        int randomNumber = random.nextInt(webClients.size());
        log.info("Random number: {}", randomNumber);
        return webClients.get(randomNumber);
    }

}
