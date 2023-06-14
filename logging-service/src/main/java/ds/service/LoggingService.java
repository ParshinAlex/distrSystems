package ds.service;

import ds.Message;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoggingService {

    String getMessages();

    void writeMessage(@RequestBody Message msg);
}
