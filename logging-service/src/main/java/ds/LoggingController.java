package ds;

import ds.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logging")
@Slf4j
public class LoggingController {

    public LoggingService loggingService;

    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }


    @GetMapping()
    public String getMessages() {
        return loggingService.getMessages();
    }

    @PostMapping()
    public void writeMessage(@RequestBody Message msg) {
        loggingService.writeMessage(msg);
    }


}
