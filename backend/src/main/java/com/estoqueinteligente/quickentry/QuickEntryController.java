package com.estoqueinteligente.quickentry;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quick-entry")
public class QuickEntryController {
    private final QuickEntryService service;
    public QuickEntryController(QuickEntryService service){this.service=service;}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public QuickEntryResponse create(@Valid @RequestBody QuickEntryRequest request){return service.create(request);}
}
