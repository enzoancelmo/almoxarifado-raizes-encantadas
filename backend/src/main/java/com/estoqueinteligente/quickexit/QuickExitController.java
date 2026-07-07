package com.estoqueinteligente.quickexit;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quick-exit")
public class QuickExitController {
    private final QuickExitService service;
    public QuickExitController(QuickExitService service){this.service=service;}
    @PostMapping public ResponseEntity<QuickExitResponse> create(@Valid @RequestBody QuickExitRequest request){QuickExitResponse created=service.create(request);return ResponseEntity.created(URI.create("/quick-exit/"+created.getId())).body(created);}
}