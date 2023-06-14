package ds.service;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import ds.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Primary
@Slf4j
public class HzLoggingService implements LoggingService {
    public HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
    public Map<UUID, String> messages = hazelcastInstance.getMap("logging_map");

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
