package com.estoqueinteligente.entrytype;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entry-types")
public class EntryTypeController {
    private final EntryTypeService service;
    public EntryTypeController(EntryTypeService service){this.service=service;}
    @GetMapping public List<EntryTypeResponse> list(@RequestParam(defaultValue="false") boolean activeOnly){return activeOnly?service.findActive():service.findAll();}
    @GetMapping("/{id}") public EntryTypeResponse get(@PathVariable Long id){return service.findById(id);}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public EntryTypeResponse create(@Valid @RequestBody EntryTypeRequest request){return service.create(request);}
    @PutMapping("/{id}") public EntryTypeResponse update(@PathVariable Long id,@Valid @RequestBody EntryTypeRequest request){return service.update(id,request);}
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
}
