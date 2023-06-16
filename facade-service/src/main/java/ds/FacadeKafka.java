package ds;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.security.auth.callback.Callback;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Service
@Slf4j
public class FacadeKafka {
    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String kafkaMessagesTopic;

    public void produce(Message message) {

        CompletableFuture<SendResult<String, Message>> future = kafkaTemplate.send(kafkaMessagesTopic, message.getId().toString(), message);
        future.whenComplete(new BiConsumer<SendResult<String,Message>,Throwable>() {
            @Override
            public void accept(SendResult<String, Message> result, Throwable u) {
                try {
                    log.info("Sent message=[{}] with offset=[{}]" , message, result.getRecordMetadata().offset());
                }
                catch (Exception e) {
                    log.info("Unable to send message=[{}] due to : {}" , message, e.getMessage());
                }
            }
        });

    }


}
