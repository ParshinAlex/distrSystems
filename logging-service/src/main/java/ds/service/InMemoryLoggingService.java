package ds.service;

import ds.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
//@Primary
@Slf4j
public class InMemoryLoggingService implements LoggingService {
    public Map<UUID, String> messages = new ConcurrentHashMap<>();

    @Override
    public String getMessages() {
        return messages.values().toString();
    }

    @Override
    public void writeMessage(@RequestBody Message msg) {
        log.info("POST logging service message: {}", msg);
        messages.put(msg.getId(), msg.getText());
    }
}
