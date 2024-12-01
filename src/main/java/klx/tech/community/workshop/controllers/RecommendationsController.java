package klx.tech.community.workshop.controllers;

import klx.tech.community.workshop.services.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationsController {

    @Autowired
    private final ChatGptService chatGptService;

    public RecommendationsController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }


    @PostMapping
    public ResponseEntity<Mono<List<String>>> getRecommendations(@RequestBody List<String> cartItems) {
        return ResponseEntity.ok(chatGptService.getRecommendations(cartItems));
    }
}
