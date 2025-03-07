package tech.wvs.magicfridgeai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.wvs.magicfridgeai.dto.FoodItemDto;
import tech.wvs.magicfridgeai.model.FoodItem;
import tech.wvs.magicfridgeai.service.FoodItemService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/food")
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody FoodItemDto data) {
        var entity = foodItemService.create(data);
        return ResponseEntity.created(URI.create("/food/" + entity.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FoodItem>> getAll() {
        var foodItems = foodItemService.findAll();
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<FoodItem> getById(@PathVariable Long id) {
        var foodItem = foodItemService.findById(id);
        return foodItem.isPresent() ?
                ResponseEntity.ok(foodItem.get()) :
                ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateById(@PathVariable Long id,
                                           @RequestBody FoodItemDto data) {
        var foodItem = foodItemService.update(id, data);

        return foodItem.isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        var deleted = foodItemService.delete(id);

        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}
