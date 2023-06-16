package ds;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessagesKafka {

    @Autowired
    private MessagesService messagesService;

    @KafkaListener(topics = "#{'${spring.kafka.topic}'}")
    public void consume(Message message){
        log.info("Consuming the message: {}", message);
        messagesService.saveMessage(message);
    }
}
