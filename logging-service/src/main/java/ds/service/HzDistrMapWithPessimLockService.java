package ds.service;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import ds.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Service
//@Primary
@Slf4j
public class HzDistrMapWithPessimLockService implements LoggingService {
    public HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
    IMap<String, Integer> messages = hazelcastInstance.getMap( "map" );
    String key = "1";

    public HzDistrMapWithPessimLockService() {}

    @Override
    public String getMessages() {
        try {
            Integer result = messages.get(key);
            return result.toString();
        } catch (Exception e) {
            log.info(e.toString());
            return "0";
        }
    }

    @Override
    public void writeMessage(Message msg) {
        log.info("POST logging service message: {}", msg);
        messages.put( key, 1 );
        System.out.println( "Starting" );
        for ( int k = 0; k < 1000; k++ ) {
            messages.lock( key );
            try {
                int value = messages.get( key );
                Thread.sleep( 10 );
                value++;
                messages.put( key, value );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                messages.unlock( key );
            }
        }
        System.out.println( "Finished! Result = " + messages.get( key ) );
    }
}
