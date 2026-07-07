package com.estoqueinteligente.eventtemplate;

import com.estoqueinteligente.common.BusinessException;
import com.estoqueinteligente.common.ResourceNotFoundException;
import com.estoqueinteligente.product.Product;
import com.estoqueinteligente.product.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventTemplateService {
    private final EventTemplateRepository repository; private final ProductRepository productRepository;
    public EventTemplateService(EventTemplateRepository repository,ProductRepository productRepository){this.repository=repository;this.productRepository=productRepository;}
    @Transactional(readOnly=true) public List<EventTemplateResponse> findAll(){return repository.findAllWithItems().stream().map(EventTemplateResponse::new).toList();}
    @Transactional(readOnly=true) public EventTemplateResponse findById(Long id){return new EventTemplateResponse(getEntityWithItems(id));}
    @Transactional(readOnly=true) public EventTemplate getEntity(Long id){return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Modelo de evento não encontrado"));}
    @Transactional(readOnly=true) public EventTemplate getEntityWithItems(Long id){return repository.findByIdWithItems(id).orElseThrow(()->new ResourceNotFoundException("Modelo de evento não encontrado"));}
    @Transactional public EventTemplateResponse create(EventTemplateRequest request){EventTemplate t=new EventTemplate();apply(t,request,true);return new EventTemplateResponse(repository.save(t));}
    @Transactional public EventTemplateResponse update(Long id,EventTemplateRequest request){EventTemplate t=getEntityWithItems(id);apply(t,request,false);return new EventTemplateResponse(repository.save(t));}
    @Transactional public void delete(Long id){EventTemplate t=getEntity(id);t.setActive(false);repository.save(t);}
    @Transactional public EventTemplateResponse addItem(Long id,EventTemplateItemRequest request){EventTemplate t=getEntityWithItems(id);Product p=productRepository.findByIdWithCategory(request.getItemId()).orElseThrow(()->new ResourceNotFoundException("Item não encontrado"));EventTemplateItem item=t.getItems().stream().filter(i->i.getProduct().getId().equals(p.getId())).findFirst().orElse(null);if(item==null){item=new EventTemplateItem();item.setEventTemplate(t);item.setProduct(p);t.getItems().add(item);}applyItem(item,request);return new EventTemplateResponse(repository.save(t));}
    @Transactional public EventTemplateResponse updateItem(Long id,Long itemId,EventTemplateItemRequest request){EventTemplate t=getEntityWithItems(id);EventTemplateItem item=t.getItems().stream().filter(i->i.getId().equals(itemId)).findFirst().orElseThrow(()->new ResourceNotFoundException("Item do modelo não encontrado"));if(!item.getProduct().getId().equals(request.getItemId())&&t.getItems().stream().anyMatch(i->i.getProduct().getId().equals(request.getItemId())))throw new BusinessException("Este item já existe no modelo.");Product p=productRepository.findByIdWithCategory(request.getItemId()).orElseThrow(()->new ResourceNotFoundException("Item não encontrado"));item.setProduct(p);applyItem(item,request);return new EventTemplateResponse(repository.save(t));}
    @Transactional public EventTemplateResponse deleteItem(Long id,Long itemId){EventTemplate t=getEntityWithItems(id);boolean removed=t.getItems().removeIf(i->i.getId().equals(itemId));if(!removed)throw new ResourceNotFoundException("Item do modelo não encontrado");return new EventTemplateResponse(repository.save(t));}
    private void apply(EventTemplate t,EventTemplateRequest r,boolean creating){t.setName(r.getName().trim());t.setDescription(normalize(r.getDescription()));t.setEventType(normalize(r.getEventType()));t.setNotes(normalize(r.getNotes()));t.setActive(r.getActive()==null?creating:r.getActive());}
    private void applyItem(EventTemplateItem item,EventTemplateItemRequest r){if(r.getSuggestedQuantity()==null||r.getSuggestedQuantity()<=0)throw new BusinessException("Quantidade sugerida deve ser maior que zero.");item.setSuggestedQuantity(r.getSuggestedQuantity());item.setNotes(normalize(r.getNotes()));}
    private String normalize(String value){return value==null||value.isBlank()?null:value.trim();}
}