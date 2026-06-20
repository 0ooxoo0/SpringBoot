package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Product> productMap;
    private final Map<UUID, Article> articleMap;

    public StorageService() {
        this.productMap = new HashMap<>();
        this.articleMap = new HashMap<>();
        initTestData();
    }

    public Collection<Product> getAllProducts() {
        return productMap.values();
    }

    public Collection<Article> getAllArticles() {
        return articleMap.values();
    }

    public List<Searchable> getAllSearchables() {
        List<Searchable> list = new ArrayList<>();
        list.addAll(productMap.values());
        list.addAll(articleMap.values());
        return list;
    }

    // ----- НОВЫЙ МЕТОД -----
    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(productMap.get(id));
    }

    private void initTestData() {
        SimpleProduct apple = new SimpleProduct(UUID.randomUUID(), "Яблоко", 100);
        DiscountedProduct bread = new DiscountedProduct(UUID.randomUUID(), "Хлеб", 50, 20);
        SimpleProduct milk = new SimpleProduct(UUID.randomUUID(), "Молоко", 80);
        FixPriceProduct cheese = new FixPriceProduct(UUID.randomUUID(), "Сыр");
        DiscountedProduct juice = new DiscountedProduct(UUID.randomUUID(), "Сок", 120, 10);
        SimpleProduct chocolate = new SimpleProduct(UUID.randomUUID(), "Шоколад", 150);

        productMap.put(apple.getId(), apple);
        productMap.put(bread.getId(), bread);
        productMap.put(milk.getId(), milk);
        productMap.put(cheese.getId(), cheese);
        productMap.put(juice.getId(), juice);
        productMap.put(chocolate.getId(), chocolate);

        Article article1 = new Article(UUID.randomUUID(), "Польза молока", "Молоко богато кальцием и белком");
        Article article2 = new Article(UUID.randomUUID(), "Рецепт сырников", "Для сырников нужен творог и сыр");
        Article article3 = new Article(UUID.randomUUID(), "Программирование на Java", "Java – объектно-ориентированный язык");

        articleMap.put(article1.getId(), article1);
        articleMap.put(article2.getId(), article2);
        articleMap.put(article3.getId(), article3);
    }
}