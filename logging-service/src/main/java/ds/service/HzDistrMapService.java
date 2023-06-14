package ds.service;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import ds.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
//@Primary
@Slf4j
public class HzDistrMapService implements LoggingService {
    public HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
    public Map<Integer, Integer> messages = hazelcastInstance.getMap("distributed_map");

    public HzDistrMapService() {}

    @Override
    public String getMessages() {
        return messages.values().toString();
    }

    @Override
    public void writeMessage(Message msg) {
        log.info("POST DistrMapService logging service message: {}", msg);
        for (int i = 0; i < 1000; i++) {
            messages.put(Integer.valueOf(i), Integer.valueOf(i));
        }
    }
}

