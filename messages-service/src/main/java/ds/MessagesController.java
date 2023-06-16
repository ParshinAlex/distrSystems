package ds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessagesController {

    @Autowired
    private MessagesService messagesService;

    @GetMapping()
    public String messages() {
        return messagesService.getMessages();
    }
}
