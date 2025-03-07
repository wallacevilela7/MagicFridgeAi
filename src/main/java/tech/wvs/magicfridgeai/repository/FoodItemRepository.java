package tech.wvs.magicfridgeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.wvs.magicfridgeai.model.FoodItem;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
}
