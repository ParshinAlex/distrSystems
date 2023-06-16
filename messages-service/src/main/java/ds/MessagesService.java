package ds;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class MessagesService {
    private final Map<UUID, String> messages = new ConcurrentHashMap<>();

    public void saveMessage(Message message) {
        messages.put(message.getId(), message.getText());
    }

    public String getMessages() {
        return messages.values().toString();
    }

}
