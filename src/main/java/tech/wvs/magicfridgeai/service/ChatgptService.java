package tech.wvs.magicfridgeai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.wvs.magicfridgeai.model.FoodItem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatgptService {

    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;


    public ChatgptService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> generateRecipe(List<FoodItem> items) {

        String foodItems = items.stream()
                .map(item -> String.format("%s (%s) - Quantidade: %d, Validade: %s",
                        item.getName(), item.getCategory(), item.getQuantity(), item.getExpiryDate()))
                .collect(Collectors.joining("\n"));

        String prompt = "Baseado no meu banco de dados faça uma receita com os seguinte ingredientes: " + foodItems;

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system",
                                "content", "Você é um assistente que cria receitas."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        return webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var choices = (List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return message.get("content").toString();
                    }
                    return "Nenhuma receita foi gerada.";
                });

    }
}
