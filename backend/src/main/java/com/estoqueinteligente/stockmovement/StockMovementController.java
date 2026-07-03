package com.estoqueinteligente.stockmovement;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-movements")
public class StockMovementController {
    private final StockMovementService service; public StockMovementController(StockMovementService service){this.service=service;}
    @GetMapping public List<StockMovementResponse> findAll(){return service.findAll();}
    @GetMapping("/{id}") public StockMovementResponse findById(@PathVariable Long id){return service.findById(id);}
    @GetMapping("/product/{productId}") public List<StockMovementResponse> findByProduct(@PathVariable Long productId){return service.findByProduct(productId);}
    @PostMapping public ResponseEntity<StockMovementResponse> create(@Valid @RequestBody StockMovementRequest request){StockMovementResponse created=service.create(request);return ResponseEntity.created(URI.create("/stock-movements/"+created.getId())).body(created);}
}
