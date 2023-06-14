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
public class HzDistrdMapWithoutLockService implements LoggingService {

    public HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
    public Map<Integer, Integer> messages = hazelcastInstance.getMap("withoutLock_map");

    public Integer key = Integer.valueOf(1);

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
        log.info("POST DistrMapWithoutLockService logging service message: {}", msg);
        try {

            messages.put(key, key);
            System.out.println( "Starting" );
            for ( int k = 0; k < 1000; k++ ) {
                if ( k % 100 == 0 ) System.out.println( "At: " + k );
                Integer value = messages.get( key );
                Thread.sleep( 10 );
                value++;
                messages.put( key, value );
            }
            Thread.sleep( 2000 );
            System.out.println( "Finished! Result = " + messages.get(key) );
            
//            messages.put(key, 0);
//            for (int k = 0; k < 1000; k++) {
//                Integer value = messages.get(key);
//                Thread.sleep(10);
//                value = value.intValue() + 1;
//                messages.put(key, value);
//            }

        } catch (Exception e) {
            messages.put(key, 0);
            log.info(e.toString());
            writeMessage(msg);
        }
    }

}