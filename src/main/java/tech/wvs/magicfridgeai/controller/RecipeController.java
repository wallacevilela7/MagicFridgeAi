package tech.wvs.magicfridgeai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import tech.wvs.magicfridgeai.model.FoodItem;
import tech.wvs.magicfridgeai.service.ChatgptService;
import tech.wvs.magicfridgeai.service.FoodItemService;

import java.util.List;

@RestController
@RequestMapping(path = "/recipe")
public class RecipeController {

    private final FoodItemService foodItemService;
    private final ChatgptService chatgptService;

    public RecipeController(FoodItemService foodItemService, ChatgptService chatgptService) {
        this.foodItemService = foodItemService;
        this.chatgptService = chatgptService;
    }

    @GetMapping(path = "/generate")
    public Mono<ResponseEntity<String>> generateRecipe(){
        List<FoodItem> items = foodItemService.findAll();
        return chatgptService.generateRecipe(items)
                .map(recipe -> ResponseEntity.ok(recipe))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
