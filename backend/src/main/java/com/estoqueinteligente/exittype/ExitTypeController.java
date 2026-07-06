package com.estoqueinteligente.exittype;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exit-types")
public class ExitTypeController {
    private final ExitTypeService service;
    public ExitTypeController(ExitTypeService service){this.service=service;}
    @GetMapping public List<ExitTypeResponse> list(@RequestParam(defaultValue="false") boolean activeOnly){return activeOnly?service.findActive():service.findAll();}
    @GetMapping("/{id}") public ExitTypeResponse get(@PathVariable Long id){return service.findById(id);}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ExitTypeResponse create(@Valid @RequestBody ExitTypeRequest request){return service.create(request);}
    @PutMapping("/{id}") public ExitTypeResponse update(@PathVariable Long id,@Valid @RequestBody ExitTypeRequest request){return service.update(id,request);}
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
}
