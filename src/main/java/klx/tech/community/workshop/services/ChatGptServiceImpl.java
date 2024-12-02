package klx.tech.community.workshop.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChatGptServiceImpl implements ChatGptService {

    private final WebClient webClient;
    private final String chatGptKey;

    @Value("${chatgpt.api.url}")
    private String apiUrl;

    public ChatGptServiceImpl(WebClient.Builder webClientBuilder, String chatGptKey) {
        this.webClient = webClientBuilder.build();
        this.chatGptKey = chatGptKey;
    }

    @Override
    public Mono<List<String>> getRecommendations(List<String> cartItems) {
        String prompt = buildPrompt(cartItems);

        return this.webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + chatGptKey)
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "model", "gpt-3.5-turbo", // Use "gpt-4" para maior qualidade
                        "messages", List.of(
                                Map.of("role", "system", "content", "Você é um especialista em recomendações de produtos."),
                                Map.of("role", "user", "content", prompt)
                        ),
                        "max_tokens", 200,
                        "temperature", 0.7
                ))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            System.err.println("Erro na chamada da API: " + errorBody);
                            return Mono.error(new Exception("Erro na chamada da API: " + errorBody));
                        }))
                .bodyToMono(Map.class)
                .map(this::parseRecommendations);
    }

    private String buildPrompt(List<String> cartItems) {
        return "Os produtos no carrinho são: " + String.join(", ", cartItems) +
                ". Sugira três produtos adicionais que poderiam ser utilizados em conjunto com os produtos no carrinho. " +
                "Responda no seguinte formato JSON estrito: { \"recommendations\": [ \"Recommendation 1\", \"Recommendation 2\", \"Recommendation 3\" ] }";
    }

    @SuppressWarnings("unchecked")
    private List<String> parseRecommendations(Map<String, Object> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Extrai a mensagem do ChatGPT
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                String content = (String) message.get("content");

                // Converte o JSON retornado em um objeto Java
                Map<String, Object> parsedJson = objectMapper.readValue(content, Map.class);

                List<String> recommendations = (List<String>) parsedJson.get("recommendations");
                log.info("Recomendations parsed: {}", recommendations);

                return recommendations;
            }
        } catch (Exception e) {
            System.err.println("Erro ao fazer o parsing da resposta: " + e.getMessage());
        }
        return List.of(); // Retorna lista vazia em caso de erro
    }

}

