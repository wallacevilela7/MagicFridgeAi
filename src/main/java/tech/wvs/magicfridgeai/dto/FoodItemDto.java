package tech.wvs.magicfridgeai.dto;

import java.time.LocalDateTime;

public record FoodItemDto(String name,
                          String category,
                          Integer quantity,
                          String expiryDate) {
}
