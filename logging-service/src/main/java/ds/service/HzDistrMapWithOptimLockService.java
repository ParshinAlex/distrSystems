package ds.service;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import ds.Message;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
//@Primary
@Slf4j
public class HzDistrMapWithOptimLockService implements LoggingService {
    public HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
    public Map<Integer, Integer> messages = hazelcastInstance.getMap("optimisticLock_map");

    public Integer key = 1;

    public HzDistrMapWithOptimLockService() {
        messages.putIfAbsent(key, 0);
    }

    @Override
    public String getMessages() {
        Integer result = messages.get(key);
        return result.toString();

    }

    @Override
    public void writeMessage(Message msg) {
        log.info("POST logging service message: {}", msg);
        for (int k = 0; k < 1000; k++) {
            writeIteration();
        }
        System.out.println( "Finished! Result = " + messages.get( key ) );
    }

    private void writeIteration() {
        try {
            for (; ; ) {
                Integer oldValue = messages.get(key);
                Integer newValue = oldValue;
                Thread.sleep(10);
                newValue = newValue + 1;
                if (messages.replace(key, oldValue, newValue))
                    break;
            }
        } catch (Exception e) {
            log.info(e.toString());
            writeIteration();
        }

    }
}
