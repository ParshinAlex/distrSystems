package ds;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/facade")
@Slf4j
public class FacadeController {

    public FacadeService facadeService;

    public FacadeController(FacadeService facadeService) {
        this.facadeService = facadeService;
    }

    @GetMapping()
    public String getMessages() {
        return facadeService.getMessages();
    }

    @PostMapping()
    public void writeMessage(@RequestBody String body) {
        facadeService.writeMessage(body);
    }

}
