package com.estoqueinteligente.eventtemplate;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event-templates")
public class EventTemplateController {
    private final EventTemplateService service;
    public EventTemplateController(EventTemplateService service){this.service=service;}
    @GetMapping public List<EventTemplateResponse> findAll(){return service.findAll();}
    @GetMapping("/{id}") public EventTemplateResponse findById(@PathVariable Long id){return service.findById(id);}
    @PostMapping public ResponseEntity<EventTemplateResponse> create(@Valid @RequestBody EventTemplateRequest request){EventTemplateResponse created=service.create(request);return ResponseEntity.created(URI.create("/event-templates/"+created.getId())).body(created);}
    @PutMapping("/{id}") public EventTemplateResponse update(@PathVariable Long id,@Valid @RequestBody EventTemplateRequest request){return service.update(id,request);}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){service.delete(id);return ResponseEntity.noContent().build();}
    @PostMapping("/{id}/items") public EventTemplateResponse addItem(@PathVariable Long id,@Valid @RequestBody EventTemplateItemRequest request){return service.addItem(id,request);}
    @PutMapping("/{id}/items/{templateItemId}") public EventTemplateResponse updateItem(@PathVariable Long id,@PathVariable Long templateItemId,@Valid @RequestBody EventTemplateItemRequest request){return service.updateItem(id,templateItemId,request);}
    @DeleteMapping("/{id}/items/{templateItemId}") public EventTemplateResponse deleteItem(@PathVariable Long id,@PathVariable Long templateItemId){return service.deleteItem(id,templateItemId);}
}