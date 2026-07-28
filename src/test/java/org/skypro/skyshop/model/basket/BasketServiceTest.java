package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.StorageService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    private BasketService basketService; // создаём вручную, чтобы избежать проблем с @InjectMocks

    @BeforeEach
    void setUp() {
        basketService = new BasketService(productBasket, storageService);
    }

    @Test
    void addProduct_shouldThrowException_whenProductNotFound() {
        UUID invalidId = UUID.randomUUID();
        when(storageService.getProductById(invalidId)).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class,
                () -> basketService.addProduct(invalidId));

        verify(productBasket, never()).addProduct(any());
    }

    @Test
    void addProduct_shouldCallBasketAdd_whenProductExists() {
        UUID validId = UUID.randomUUID();
        Product product = new SimpleProduct(validId, "Milk", 80);
        when(storageService.getProductById(validId)).thenReturn(Optional.of(product));

        basketService.addProduct(validId);

        verify(productBasket, times(1)).addProduct(validId);
    }

    @Test
    void getUserBasket_shouldReturnEmpty_whenBasketIsEmpty() {
        when(productBasket.getProducts()).thenReturn(Map.of());

        UserBasket userBasket = basketService.getUserBasket();

        assertThat(userBasket.getItems()).isEmpty();
        assertThat(userBasket.getTotal()).isZero();
    }

    @Test
    void getUserBasket_shouldReturnCorrectBasket_whenBasketHasItems() {
        UUID productId = UUID.randomUUID();
        int quantity = 2;
        int price = 100;
        Product product = new SimpleProduct(productId, "Cheese", price);

        when(productBasket.getProducts()).thenReturn(Map.of(productId, quantity));
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        UserBasket userBasket = basketService.getUserBasket();

        assertThat(userBasket.getItems()).hasSize(1);
        assertThat(userBasket.getItems().get(0).getProduct()).isEqualTo(product);
        assertThat(userBasket.getItems().get(0).getQuantity()).isEqualTo(quantity);
        assertThat(userBasket.getTotal()).isEqualTo(price * quantity);
    }
}