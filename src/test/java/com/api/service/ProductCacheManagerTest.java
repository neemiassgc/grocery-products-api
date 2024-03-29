package com.api.service;

import com.api.Resources;
import com.api.entity.Product;
import com.api.service.interfaces.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public final class ProductCacheManagerTest {

    private CacheManager<Product, UUID> productCacheManager;
    private ProductService productService;

    @BeforeEach
    void setup() {
        productCacheManager = new CacheManager<>(Product::getId);
        productService = mock(ProductService.class);
    }

    @Test
    void given_a_key_should_return_all_products_from_cache_in_the_order() {
        given(productService.findAllWithLatestPrice())
            .willReturn(getProductsByIndexes(0, 10, 4, 7, 9, 2, 3, 11));
        final String key = "eight";

        final Optional<List<Product>> actualProducts = productCacheManager.sync(key, productService::findAllWithLatestPrice);

        assertThat(actualProducts).isNotNull();
        assertThat(actualProducts).isPresent();
        assertThat(actualProducts.get()).satisfies(products -> {
            assertThat(products).hasSize(8);
            assertThat(products).extracting(Product::getDescription)
                .containsExactly(
                    "ACHOC PO NESCAU 800G", "MAIONESE QUERO 210G TP", "BISC WAFER TODDY 132G CHOC",
                    "CAFE UTAM 500G", "LIMP M.USO OMO 500ML DESINF HERBAL", "BALA GELATINA FINI 500G BURGUER",
                    "BISC ROSQ MARILAN 350G INT", "MILHO VDE PREDILECTA 170G LT"
                );
        });

        verify(productService, times(1)).findAllWithLatestPrice();
        verify(productService, only()).findAllWithLatestPrice();
    }

    @Test
    void when_list_to_sync_is_empty_then_should_return_an_empty_optional() {
        final Optional<List<Product>> actualListOfProducts = productCacheManager.sync("sigma", Collections::emptyList);

        assertThat(actualListOfProducts).isNotNull();
        assertThat(actualListOfProducts).isEmpty();
    }

    @Test
    void when_null_is_passed_in_to_sync_then_should_return_an_empty_optional() {
        final Optional<List<Product>> actualListOfProducts = productCacheManager.sync("sigma", () -> null);

        assertThat(actualListOfProducts).isNotNull();
        assertThat(actualListOfProducts).isEmpty();
    }

    @Test
    void should_return_true_if_a_key_exists_in_the_cache() {
        final List<Product> products = getProductsByIndexes(0, 4, 2, 1, 11, 9, 6);
        productCacheManager.sync("products", () -> products);

        final boolean actualState = productCacheManager.containsKey("products");

        assertThat(actualState).isTrue();
    }

    @Test
    void should_return_false_if_a_key_does_not_exist_in_the_cache() {
        final List<Product> products = getProductsByIndexes(0, 4, 2, 1, 11, 9, 6);
        productCacheManager.sync("products", () -> products);

        final boolean actualState = productCacheManager.containsKey("zeta");

        assertThat(actualState).isFalse();
    }

    @Test
    void when_evictAll_is_invoked_then_should_clean_the_whole_cache() {
        given(productService.findAllWithLatestPrice())
            .willReturn(getProductsByIndexes(0, 10, 4, 7, 9, 2, 3, 11));
        productCacheManager.sync("products", productService::findAllWithLatestPrice);
        productCacheManager.evictAll();
        productCacheManager.sync("products", productService::findAllWithLatestPrice);
        productCacheManager.sync("products", productService::findAllWithLatestPrice);

        verify(productService, times(2)).findAllWithLatestPrice();
    }

    private List<Product> getProductsByIndexes(final int ...indexes) {
        final Product[] productsToReturn = new Product[indexes.length];
        for (int i = 0; i < productsToReturn.length; i++)
            productsToReturn[i] = Resources.PRODUCTS_SAMPLE.get(indexes[i]);
        return Arrays.asList(productsToReturn);
    }
}