package org.skypro.skyshop.service;

import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final ProductBasket basket;
    private final StorageService storageService;

    public BasketService(ProductBasket basket, StorageService storageService) {
        this.basket = basket;
        this.storageService = storageService;
    }

    public void addProduct(UUID id) {
        storageService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Продукт с id " + id + " не найден"));
        basket.addProduct(id);
    }

    public UserBasket getUserBasket() {
        List<BasketItem> items = basket.getProducts().entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    int quantity = entry.getValue();
                    Product product = storageService.getProductById(productId)
                            .orElseThrow(() -> new IllegalStateException("Продукт с id " + productId + " не найден в хранилище"));
                    return new BasketItem(product, quantity);
                })
                .collect(Collectors.toList());
        return new UserBasket(items);
    }
}