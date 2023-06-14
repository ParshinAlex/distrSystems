package ds.service;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import ds.Message;
@Service
//@Primary
@Slf4j
public class HzQueueService implements LoggingService {

    public HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
    public IQueue<Integer> messages = hazelcastInstance.getQueue("queue");


    @Override
    public String getMessages() {
        LinkedList<Integer> result = new LinkedList<>();
        while ( true ) {
            try {
                int item = messages.take();
                System.out.println("Consumed: " + item);
                result.push(item);
                if (item == -1) {
                    messages.put(-1);
                    break;
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                log.info(e.toString());
            }
        }
        return result.toString();
    }

    @Override
    public void writeMessage(Message msg) {
        try{
            for ( int k = 1; k < 10; k++ ) {
                messages.put( k );
                System.out.println( "Producing: " + k );
                Thread.sleep(1000);
            }
            messages.put( -1 );
            System.out.println( "Producer Finished!" );
        } catch (Exception e) {
            log.info(e.toString());
        }

    }
}
