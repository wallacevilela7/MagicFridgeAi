package tech.wvs.magicfridgeai.service;

import org.springframework.stereotype.Service;
import tech.wvs.magicfridgeai.dto.FoodItemDto;
import tech.wvs.magicfridgeai.exception.MagicFrigdeAiException;
import tech.wvs.magicfridgeai.model.FoodItem;
import tech.wvs.magicfridgeai.repository.FoodItemRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final FoodItemRepository foodItemRepository;

    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    public FoodItem create(FoodItemDto data) {
        var item = new FoodItem();
        item.setName(data.name());
        item.setCategory(data.category());
        item.setQuantity(data.quantity());
        LocalDate localDate = LocalDate.parse(data.expiryDate(), formatter);
        LocalDateTime date = localDate.atStartOfDay();
        item.setExpiryDate(date);

        return foodItemRepository.save(item);
    }

    public List<FoodItem> findAll() {
        return foodItemRepository.findAll();
    }


    public Optional<FoodItem> findById(Long id) {
        return foodItemRepository.findById(id);
    }

    public Optional<FoodItem> update(Long id, FoodItemDto data) {
        //1: validar se o item existe
        var item = foodItemRepository.findById(id);

        if (item.isPresent()) {

            //2: verificar os campos alterados e atualizar
            updateFields(data, item.get());

            //3: salvar no banco de dados
            foodItemRepository.save(item.get());
        }

        return item;
    }

    private void updateFields(FoodItemDto data, FoodItem item) {
        if (data.name() != null && !data.name().isEmpty()  ) {
            item.setName(data.name());
        }

        if (data.category() != null &&  !data.category().isEmpty()) {
            item.setCategory(data.category());
        }

        if (data.quantity() != null) {
            item.setQuantity(data.quantity());
        }

        if (data.expiryDate() != null && !data.expiryDate().isEmpty() ) {
            LocalDate localDate = LocalDate.parse(data.expiryDate(), formatter);
            LocalDateTime date = localDate.atStartOfDay();
            item.setExpiryDate(date);
        }
    }

    public boolean delete(Long id) {
        var exists = foodItemRepository.existsById(id);

        if (exists) {
            foodItemRepository.deleteById(id);
        }

        return exists;
    }
}
