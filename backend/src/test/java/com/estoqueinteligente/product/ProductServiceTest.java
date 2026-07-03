package com.estoqueinteligente.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ProductServiceTest {
    private final ProductService service = new ProductService(null, null, null);
    @Test void shouldPrioritizeExpiredProduct() {
        Product product = product(0, 10, LocalDate.now().minusDays(1));
        assertEquals(ProductStatus.VENCIDO, service.calculateStatus(product));
    }
    @Test void shouldPrioritizeExpiringProductOverLowStock() {
        Product product = product(0, 10, LocalDate.now().plusDays(30));
        assertEquals(ProductStatus.VENCENDO, service.calculateStatus(product));
    }
    @Test void shouldIdentifyLowStock() {
        Product product = product(10, 10, null);
        assertEquals(ProductStatus.ESTOQUE_BAIXO, service.calculateStatus(product));
    }
    @Test void shouldIdentifyNormalStock() {
        Product product = product(11, 10, null);
        assertEquals(ProductStatus.NORMAL, service.calculateStatus(product));
    }
    private Product product(int quantity, int minimumStock, LocalDate expirationDate) {
        Product product = new Product(); product.setQuantity(quantity); product.setMinimumStock(minimumStock); product.setExpirationDate(expirationDate); return product;
    }
}
