package com.estoqueinteligente.product;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.estoqueinteligente.category.*;
import com.estoqueinteligente.common.*;
import com.estoqueinteligente.supplier.SupplierService;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    public ProductService(ProductRepository repository, CategoryService categoryService, SupplierService supplierService) { this.repository = repository; this.categoryService = categoryService; this.supplierService = supplierService; }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return repository.findAllWithCategory().stream().map(product -> ProductResponse.from(product, calculateStatus(product))).toList();
    }
    @Transactional(readOnly = true)
    public List<ProductResponse> findByCurrentStatus(ProductStatus status) {
        return repository.findAllWithCategory().stream()
            .filter(product -> calculateStatus(product) == status)
            .map(product -> ProductResponse.from(product, status)).toList();
    }
    @Transactional(readOnly = true)
    public List<ProductResponse> findLowStock() {
        return repository.findAllWithCategory().stream()
            .filter(product -> product.getQuantity() <= product.getMinimumStock())
            .map(product -> ProductResponse.from(product, calculateStatus(product))).toList();
    }
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = getEntity(id);
        return ProductResponse.from(product, calculateStatus(product));
    }
    @Transactional
    public ProductResponse create(ProductRequest request) {
        validateBarcode(request.barcode(), null);
        Product product = new Product();
        apply(product, request);
        product.setStatus(calculateStatus(product));
        return ProductResponse.from(repository.save(product), product.getStatus());
    }
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getEntity(id);
        validateBarcode(request.barcode(), id);
        apply(product, request);
        product.setStatus(calculateStatus(product));
        return ProductResponse.from(repository.save(product), product.getStatus());
    }
    @Transactional public void delete(Long id) { repository.delete(getEntity(id)); }
    private Product getEntity(Long id) { return repository.findByIdWithCategory(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado")); }
    private void apply(Product product, ProductRequest request) {
        product.setName(request.name().trim());
        product.setBarcode(normalizeBarcode(request.barcode()));
        product.setCategory(categoryService.getEntity(request.categoryId()));
        product.setSupplier(request.supplierId() == null ? null : supplierService.getEntity(request.supplierId()));
        product.setCostPrice(request.costPrice()); product.setSalePrice(request.salePrice());
        product.setQuantity(request.quantity()); product.setMinimumStock(request.minimumStock());
        product.setExpirationDate(request.expirationDate());
    }
    private void validateBarcode(String barcode, Long currentId) {
        String normalized = normalizeBarcode(barcode);
        if (normalized == null) return;
        boolean exists = currentId == null ? repository.existsByBarcode(normalized) : repository.existsByBarcodeAndIdNot(normalized, currentId);
        if (exists) throw new BusinessException("Já existe um produto com este código de barras");
    }
    private String normalizeBarcode(String barcode) { return barcode == null || barcode.isBlank() ? null : barcode.trim(); }
    public ProductStatus calculateStatus(Product product) {
        LocalDate today = LocalDate.now();
        LocalDate expiration = product.getExpirationDate();
        if (expiration != null && expiration.isBefore(today)) return ProductStatus.VENCIDO;
        if (expiration != null && !expiration.isAfter(today.plusDays(30))) return ProductStatus.VENCENDO;
        if (product.getQuantity() <= product.getMinimumStock()) return ProductStatus.ESTOQUE_BAIXO;
        return ProductStatus.NORMAL;
    }
}
