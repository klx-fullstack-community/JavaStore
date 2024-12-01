package klx.tech.community.workshop.services;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ChatGptService {
    Mono<List<String>> getRecommendations(List<String> cartItems);
}
