package com.estoqueinteligente.product;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }
    @GetMapping public List<ProductResponse> findAll() { return service.findAll(); }
    @GetMapping("/{id}") public ProductResponse findById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/products/" + created.id())).body(created);
    }
    @PutMapping("/{id}") public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
