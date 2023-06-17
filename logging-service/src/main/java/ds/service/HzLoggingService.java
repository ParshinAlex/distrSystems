package ds.service;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import ds.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Primary
@Slf4j
public class HzLoggingService implements LoggingService {
    public HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    public Map<UUID, String> messages;

    public HzLoggingService(@Value("${spring.consul.hazelcast.map}") String loggingMap) {
        messages = hazelcastInstance.getMap(loggingMap);

    }

    @Override
    public String getMessages() {
        return messages.values().toString();
    }

    @Override
    public void writeMessage(Message msg) {
        log.info("POST logging service message: {}", msg);
        messages.put(msg.getId(), msg.getText());
    }
}
