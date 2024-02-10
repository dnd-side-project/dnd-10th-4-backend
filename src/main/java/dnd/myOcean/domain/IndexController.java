package dnd.myOcean.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    
    @GetMapping("/home")
    public String home() {
        return "hi";
    }
}
