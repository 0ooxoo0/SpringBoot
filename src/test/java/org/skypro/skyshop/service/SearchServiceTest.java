package org.skypro.skyshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.StorageService;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    @Test
    void shouldReturnEmptyListWhenStorageIsEmpty() {
        // given
        when(storageService.getAllSearchables()).thenReturn(List.of());

        // when
        List<SearchResult> results = searchService.search("anything");

        // then
        assertThat(results).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchesFound() {
        // given
        Searchable apple = new SimpleProduct(UUID.randomUUID(), "Apple", 100);
        Searchable banana = new SimpleProduct(UUID.randomUUID(), "Banana", 80);
        when(storageService.getAllSearchables()).thenReturn(List.of(apple, banana));

        // when
        List<SearchResult> results = searchService.search("Orange");

        // then
        assertThat(results).isEmpty();
    }

    @Test
    void shouldReturnSingleResultWhenMatchFound() {
        // given
        Searchable testProduct = new SimpleProduct(UUID.randomUUID(), "TestProduct", 150);
        Searchable other = new SimpleProduct(UUID.randomUUID(), "Other", 200);
        when(storageService.getAllSearchables()).thenReturn(List.of(testProduct, other));

        // when
        List<SearchResult> results = searchService.search("Test");

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("TestProduct");
    }
}